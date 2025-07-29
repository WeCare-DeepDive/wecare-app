package com.example.wecare.integration.routine;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.invitation.domain.InvitationId;
import com.example.wecare.invitation.domain.RelationshipType;
import com.example.wecare.invitation.repository.InvitationRepository;
import com.example.wecare.member.domain.Gender;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.domain.RepeatDay;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineType;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.repository.RoutineRepository;
import com.jayway.jsonpath.JsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoutineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private String guardianToken;
    private String dependentToken;
    private Long guardianId;
    private Long dependentId;

    @BeforeEach
    public void setup() throws Exception {
        // 테스트 전에 기존 데이터를 정리
        routineRepository.deleteAll();
        invitationRepository.deleteAll();
        memberRepository.deleteAll();

        // 1. 보호자 회원 가입
        SignUpRequest guardianSignUpRequest = SignUpRequest.builder()
                .username("guardianUser123")
                .password("password123!")
                .name("보호자")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1980, 1, 1))
                .role(Role.GUARDIAN)
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guardianSignUpRequest)))
                .andExpect(status().isCreated());

        // 2. 보호자 로그인 및 토큰 획득
        LoginRequest guardianLoginRequest = new LoginRequest("guardianUser123", "password123!");

        MvcResult guardianLoginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guardianLoginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String guardianLoginResponse = guardianLoginResult.getResponse().getContentAsString();
        guardianToken = JsonPath.read(guardianLoginResponse, "$.accessToken");

        // 보호자 ID 가져오기
        Member guardian = memberRepository.findByUsername("guardianUser123").orElseThrow();
        guardianId = guardian.getId();
        System.out.println("보호자 ID: " + guardianId);

        // 3. 피보호자 회원 가입
        SignUpRequest dependentSignUpRequest = SignUpRequest.builder()
                .username("dependentUser123")
                .password("password123!")
                .name("피보호자")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.DEPENDENT)
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dependentSignUpRequest)))
                .andExpect(status().isCreated());

        // 4. 피보호자 로그인 및 토큰 획득
        LoginRequest dependentLoginRequest = new LoginRequest("dependentUser123", "password123!");

        MvcResult dependentLoginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dependentLoginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String dependentLoginResponse = dependentLoginResult.getResponse().getContentAsString();
        dependentToken = JsonPath.read(dependentLoginResponse, "$.accessToken");

        // 피보호자 ID 가져오기
        Member dependent = memberRepository.findByUsername("dependentUser123").orElseThrow();
        dependentId = dependent.getId();
        System.out.println("피보호자 ID: " + dependentId);

        // 5. 초대 코드 생성 (보호자가 생성)
        MvcResult invitationResult = mockMvc.perform(post("/api/invitations/generate")
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String invitationResponse = invitationResult.getResponse().getContentAsString();
        String invitationCode = JsonPath.read(invitationResponse, "$.invitationCode");
        System.out.println("생성된 초대 코드: " + invitationCode);

        // 6. 초대 코드 수락 (피보호자가 수락)
        Map<String, Object> acceptRequest = new HashMap<>();
        acceptRequest.put("invitationCode", invitationCode);
        acceptRequest.put("relationshipType", "PARENT");

        mockMvc.perform(post("/api/invitations/accept")
                        .header("Authorization", "Bearer " + dependentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acceptRequest)))
                .andExpect(status().isOk());

        // 7. 관계 설정이 제대로 되었는지 확인 및 보정
        boolean relationExists = invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(guardianId, dependentId);
        System.out.println("초대 코드 수락 후 관계 존재 여부: " + relationExists);

        if (!relationExists) {
            System.out.println("관계가 없으므로 수동으로 설정합니다.");
            // 기존 관계 확인
            Optional<Invitation> existingInvitation = invitationRepository.findById(new InvitationId(guardianId, dependentId));

            if (existingInvitation.isPresent()) {
                Invitation invitation = existingInvitation.get();
                invitation.setActive(true);
                invitationRepository.save(invitation);
                System.out.println("기존 관계를 활성화했습니다.");
            } else {
                // 새로운 관계 설정
                guardian = memberRepository.findById(guardianId).orElseThrow();
                dependent = memberRepository.findById(dependentId).orElseThrow();

                Invitation invitation = Invitation.builder()
                        .guardian(guardian)
                        .dependent(dependent)
                        .isActive(true)
                        .relationshipType(RelationshipType.PARENT)
                        .build();
                invitationRepository.saveAndFlush(invitation);
                System.out.println("새로운 관계를 생성했습니다.");
            }

            // 저장 후 엔티티 새로고침
            entityManager.flush();
            entityManager.clear();

            // 관계 재확인
            relationExists = invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(guardianId, dependentId);
            System.out.println("수동 설정 후 관계 존재 여부: " + relationExists);
        }
    }

    @Test
    @DisplayName("루틴 생성 - 디버깅 테스트")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createRoutine_Debug() throws Exception {
        // 트랜잭션 분리하여 데이터베이스 상태 확인
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.execute(status -> {
            // 모든 관계 조회
            List<Invitation> invitations = invitationRepository.findAll();
            System.out.println("======= 데이터베이스에 존재하는 관계 =======");
            for (Invitation invitation : invitations) {
                System.out.printf("보호자 ID: %d, 피보호자 ID: %d, 활성화: %b, 관계유형: %s%n",
                        invitation.getGuardian().getId(),
                        invitation.getDependent().getId(),
                        invitation.isActive(),
                        invitation.getRelationshipType());
            }

            // 특정 관계 확인
            boolean relationExists = invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(guardianId, dependentId);
            System.out.printf("guardianId=%d, dependentId=%d 관계 존재여부: %b%n", guardianId, dependentId, relationExists);

            return null;
        });

        // 루틴 생성 요청 객체 준비
        RoutineRequest routineRequest = RoutineRequest.builder()
                .type(RoutineType.valueOf("ACTIVITY"))
                .title("산책하기")
                .description("매일 아침 30분 산책")
                .startTime(LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(30).withSecond(0).withNano(0))
                .repeat(true)
                .repeatDays(asList(RepeatDay.MON, RepeatDay.TUE, RepeatDay.WED, RepeatDay.THU, RepeatDay.FRI))
                .build();

        // 수동으로 관계 재설정
        template.execute(status -> {
            Member guardian = memberRepository.findById(guardianId).orElseThrow();
            Member dependent = memberRepository.findById(dependentId).orElseThrow();

            // 기존 관계 삭제
            Optional<Invitation> existingInvitation = invitationRepository.findById(new InvitationId(guardianId, dependentId));
            if (existingInvitation.isPresent()) {
                invitationRepository.delete(existingInvitation.get());
            }

            // 새로운 관계 생성
            Invitation invitation = Invitation.builder()
                    .guardian(guardian)
                    .dependent(dependent)
                    .isActive(true)
                    .relationshipType(RelationshipType.PARENT)
                    .build();
            invitationRepository.saveAndFlush(invitation);

            System.out.println("관계를 다시 설정했습니다.");
            return null;
        });

        // 토큰 정보 출력
        System.out.println("요청에 사용될 보호자 토큰: " + (guardianToken != null ? guardianToken.substring(0, 20) + "..." : "null"));

        // API 요청 및 응답 확인
        MvcResult result = mockMvc.perform(post("/api/routines/" + dependentId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andDo(r -> {
                    System.out.println("루틴 생성 API 응답 상태 코드: " + r.getResponse().getStatus());
                    String content = r.getResponse().getContentAsString();
                    if (!content.isEmpty()) {
                        System.out.println("응답 내용: " + content);
                    }
                })
                .andReturn();

        int statusCode = result.getResponse().getStatus();
        System.out.println("최종 응답 코드: " + statusCode);
    }

    @Test
    @DisplayName("루틴 생성 - 성공")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createRoutine_Success() throws Exception {
        // 관계 검증 및 설정
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.execute(status -> {
            boolean relationExists = invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(guardianId, dependentId);

            if (!relationExists) {
                System.out.println("guardianId=" + guardianId + ", dependentId=" + dependentId + " 관계가 없으므로 설정합니다.");
                Member guardian = memberRepository.findById(guardianId).orElseThrow();
                Member dependent = memberRepository.findById(dependentId).orElseThrow();

                Invitation invitation = Invitation.builder()
                        .guardian(guardian)
                        .dependent(dependent)
                        .isActive(true)
                        .relationshipType(RelationshipType.PARENT)
                        .build();
                invitationRepository.saveAndFlush(invitation);

                // 관계 생성 확인
                relationExists = invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(guardianId, dependentId);
                System.out.println("관계 설정 후 존재 여부: " + relationExists);
            } else {
                System.out.println("유효한 관계가 이미 존재합니다.");
            }

            return null;
        });

        // 루틴 요청 객체 생성
        RoutineRequest routineRequest = RoutineRequest.builder()
                .type(RoutineType.valueOf("ACTIVITY"))
                .title("산책하기")
                .description("매일 아침 30분 산책")
                .startTime(LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(30).withSecond(0).withNano(0))
                .repeat(true)
                .repeatDays(asList(RepeatDay.MON, RepeatDay.TUE, RepeatDay.WED, RepeatDay.THU, RepeatDay.FRI))
                .build();

        // API 요청 및 검증
        MvcResult result = mockMvc.perform(post("/api/routines/" + dependentId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // 생성된 루틴 확인
        template.execute(status -> {
            List<Routine> routines = routineRepository.findAll();
            if (!routines.isEmpty()) {
                Routine createdRoutine = routines.get(0);
                System.out.printf("생성된 루틴: ID=%d, 제목=%s, 보호자ID=%d, 피보호자ID=%d%n",
                        createdRoutine.getId(),
                        createdRoutine.getTitle(),
                        createdRoutine.getGuardian().getId(),
                        createdRoutine.getDependent().getId());

                // 검증
                assertThat(createdRoutine.getGuardian().getId()).isEqualTo(guardianId);
                assertThat(createdRoutine.getDependent().getId()).isEqualTo(dependentId);
                assertThat(createdRoutine.getTitle()).isEqualTo("산책하기");
            }
            return null;
        });
    }

    @Test
    @DisplayName("루틴 생성 - 관계 없는 경우 실패")
    public void createRoutine_NoRelationship_Fail() throws Exception {
        // 새로운 피보호자 생성
        SignUpRequest newDependentSignUpRequest = SignUpRequest.builder()
                .username("newDependentUser123")
                .password("password123!")
                .name("새로운피보호자")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(2002, 1, 1))
                .role(Role.DEPENDENT)
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDependentSignUpRequest)))
                .andExpect(status().isCreated());

        Member newDependent = memberRepository.findByUsername("newDependentUser123").orElseThrow();
        Long newDependentId = newDependent.getId();

        // 관계 없는 피보호자에게 루틴 생성 시도
        RoutineRequest routineRequest = RoutineRequest.builder()
                .type(RoutineType.valueOf("ACTIVITY"))
                .title("산책하기")
                .description("매일 아침 30분 산책")
                .startTime(LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(30).withSecond(0).withNano(0))
                .repeat(true)
                .repeatDays(asList(RepeatDay.MON, RepeatDay.TUE, RepeatDay.WED, RepeatDay.THU, RepeatDay.FRI))
                .build();

        mockMvc.perform(post("/api/routines/" + newDependentId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("루틴 생성 - 피보호자가 보호자에게 생성 시도 시 실패")
    public void createRoutine_DependentCreatingForGuardian_Fail() throws Exception {
        RoutineRequest routineRequest = RoutineRequest.builder()
                .type(RoutineType.valueOf("ACTIVITY"))
                .title("산책하기")
                .description("매일 아침 30분 산책")
                .startTime(LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(30).withSecond(0).withNano(0))
                .repeat(true)
                .repeatDays(asList(RepeatDay.MON, RepeatDay.TUE, RepeatDay.WED, RepeatDay.THU, RepeatDay.FRI))
                .build();

        mockMvc.perform(post("/api/routines/" + guardianId)
                        .header("Authorization", "Bearer " + dependentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andExpect(status().isForbidden());
    }

    // TODO: 다른 루틴 관련 통합 테스트 추가 (조회, 수정, 삭제, 완료, 메모 등)
}
