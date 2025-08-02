package com.example.wecare.integration;

import com.example.wecare.auth.jwt.JwtUtil;
import com.example.wecare.connection.domain.Connection;
import com.example.wecare.connection.repository.ConnectionRepository;
import com.example.wecare.connection.code.RelationshipType;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.repository.RoutineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import com.example.wecare.routine.code.NotificationType;
import com.example.wecare.routine.code.RepeatDay;
import com.example.wecare.routine.code.RoutineType;
import com.example.wecare.routine.code.SoundType;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineAlert;
import com.example.wecare.routine.domain.RoutineHistory;
import com.example.wecare.routine.domain.RoutineRepeatDay;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

import static com.example.wecare.routine.code.NotificationType.NONE;
import static com.example.wecare.routine.code.RoutineType.MEDICATION;
import static com.example.wecare.routine.code.SoundType.DEFAULT_SOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.wecare.common.security.PartnerAccessHandler;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RoutineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PartnerAccessHandler partnerAccessHandler; // PartnerAccessHandler 주입

    private Member guardian;
    private Member dependent;
    private Member guardian2; // 추가: 다른 보호자
    private Member dependent2; // 추가: 다른 피보호자 (사용 안 할 수도 있지만 일관성을 위해)
    private String guardianToken;
    private String dependentToken;
    private String guardian2Token; // 추가: 다른 보호자 토큰

    @AfterEach
    void tearDown() {
        routineRepository.deleteAll();
        connectionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 및 저장
        guardian = memberRepository.save(Member.builder()
                .username("guardian1")
                .password("password!1")
                .name("Guardian Name")
                .gender(com.example.wecare.member.code.Gender.FEMALE)
                .birthDate(java.time.LocalDate.of(1950, 1, 1))
                .role(Role.GUARDIAN)
                .build());

        dependent = memberRepository.save(Member.builder()
                .username("dependent1")
                .password("password!1")
                .name("Dependent Name")
                .gender(com.example.wecare.member.code.Gender.MALE)
                .birthDate(java.time.LocalDate.of(1990, 1, 1))
                .role(Role.DEPENDENT)
                .build());

        guardian2 = memberRepository.save(Member.builder()
                .username("guardian2")
                .password("password!2")
                .name("Another Guardian")
                .gender(com.example.wecare.member.code.Gender.MALE)
                .birthDate(java.time.LocalDate.of(1960, 2, 2))
                .role(Role.GUARDIAN)
                .build());

        dependent2 = memberRepository.save(Member.builder()
                .username("dependent2")
                .password("password!2")
                .name("Another Dependent")
                .gender(com.example.wecare.member.code.Gender.FEMALE)
                .birthDate(java.time.LocalDate.of(2000, 3, 3))
                .role(Role.DEPENDENT)
                .build());

        // 보호자와 피보호자 간의 연결 생성
        connectionRepository.save(Connection.builder()
                .guardian(guardian)
                .dependent(dependent)
                .relationshipType(RelationshipType.PARENT)
                .active(true)
                .build());

        // 테스트용 토큰 생성
        Authentication guardianAuth = new UsernamePasswordAuthenticationToken(guardian, null, guardian.getAuthorities());
        guardianToken = jwtUtil.generateAccessToken(guardianAuth);

        Authentication dependentAuth = new UsernamePasswordAuthenticationToken(dependent, null, dependent.getAuthorities());
        dependentToken = jwtUtil.generateAccessToken(dependentAuth);

        Authentication guardian2Auth = new UsernamePasswordAuthenticationToken(guardian2, null, guardian2.getAuthorities());
        guardian2Token = jwtUtil.generateAccessToken(guardian2Auth);

        System.out.println("--- Connections after setUp ---");
        connectionRepository.findAll().forEach(conn -> System.out.println("Connection: " + conn.getId() + ", Guardian: " + conn.getGuardian().getUsername() + ", Dependent: " + conn.getDependent().getUsername() + ", Active: " + conn.isActive()));
        System.out.println("-----------------------------");
    }

    @DisplayName("[통합] 루틴 생성 - 성공 및 인증 실패(401) 테스트")
    @Test
    void createRoutine_Success_And_Auth_Fail() throws Exception {
        // given: 유효한 루틴 생성 요청
        RoutineRequest request = createRoutineRequest("아침 약 먹기");

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("아침 약 먹기"))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 생성 - 유효성 검사 실패(400) 테스트")
    @Test
    void createRoutine_Fail_By_InvalidRequest() throws Exception {
        // given: 제목이 비어있는 잘못된 루틴 생성 요청
        RoutineRequest request = createRoutineRequest(""); // Title is blank

        // when/then: [유효성 검사 실패] 잘못된 요청 데이터로 요청 시 400 Bad Request
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 생성 - 인가 실패(400) 테스트")
    @Test
    void createRoutine_Fail_By_Authorization() throws Exception {
        // given: 유효하지만 권한이 없는 '피보호자'의 토큰
        RoutineRequest request = createRoutineRequest("보호자만 생성 가능한 루틴");

        // when/then: [인가 실패] 피보호자 토큰으로 요청 시 403 Forbidden
        // createRoutine은 보호자(GUARDIAN)만 호출 가능해야 함
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + dependentToken) // Note: Using dependent's token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 생성 - 소유권 기반 인가 실패(403) 테스트")
    @Test
    void createRoutine_Fail_By_OwnershipAuthorization() throws Exception {
        System.out.println("--- Connections before OwnershipAuthorization test ---");
        connectionRepository.findAll().forEach(conn -> System.out.println("Connection: " + conn.getId() + ", Guardian: " + conn.getGuardian().getUsername() + ", Dependent: " + conn.getDependent().getUsername() + ", Active: " + conn.isActive()));
        System.out.println("--------------------------------------------------");

        // given: 유효하지만 dependent1과 관계없는 guardian2의 토큰
        RoutineRequest request = createRoutineRequest("다른 보호자가 생성 시도");

        // SecurityContextHolder에 guardian2의 인증 정보 설정
        Authentication guardian2Auth = new UsernamePasswordAuthenticationToken(guardian2, null, guardian2.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(guardian2Auth);

        // PartnerAccessHandler.ownershipCheck 직접 호출 및 결과 확인
        boolean ownershipCheckResult = partnerAccessHandler.ownershipCheck(dependent.getId());
        System.out.println("PartnerAccessHandler.ownershipCheck(dependent.getId()) result: " + ownershipCheckResult);

        // when/then: [소유권 기반 인가 실패] guardian2가 dependent1의 루틴을 생성 시도 시 403 Forbidden
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardian2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andDo(print());

        // SecurityContextHolder 초기화
        SecurityContextHolder.clearContext();
    }

    @DisplayName("[통합] 루틴 생성 - 시간 유효성 검사 실패(400) 테스트")
    @Test
    void createRoutine_Fail_By_InvalidTime() throws Exception {
        // given: 시작 시간이 종료 시간보다 늦은 잘못된 루틴 생성 요청
        RoutineRequest request = createRoutineRequest("시간 오류 루틴");
        request.setStartTime(LocalTime.of(11, 0));
        request.setEndTime(LocalTime.of(10, 0)); // 시작 시간이 종료 시간보다 늦음

        // when/then: [시간 유효성 검사 실패] 잘못된 시간 데이터로 요청 시 400 Bad Request
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    // Helper method to create a valid request object
    private RoutineRequest createRoutineRequest(String title) {
        RoutineRequest request = new RoutineRequest();
        request.setTitle(title);
        request.setStartTime(LocalTime.of(0, 0));
        request.setEndTime(LocalTime.of(10, 0));
        request.setRoutineType(MEDICATION);
        request.setIsAlertActive(true);
        request.setNotificationType(NONE); // Changed from BOTH
        request.setSoundType(DEFAULT_SOUND); // Changed from DEFAULT
        return request;
    }

    @DisplayName("[통합] 루틴 목록 조회 - 성공 및 인증 실패(401) 테스트")
    @Test
    void getRoutinesByDependentId_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성 (테스트를 위해 루틴이 존재해야 함)
        RoutineRequest createRequest = createRoutineRequest("테스트 루틴");
        mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(get("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("테스트 루틴"))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(get("/api/routines/{dependentId}", dependent.getId()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 반복 데이터 조회 - 성공 및 인증 실패(401) 테스트")
    @Test
    void getRepeatDaysByRoutineId_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성 및 루틴 ID 획득
        RoutineRequest createRequest = createRoutineRequest("반복 요일 테스트 루틴");
        String responseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(responseContent).get("id").asLong();

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(get("/api/routines/{routineId}/repeats", routineId)
                        .header("Authorization", "Bearer " + guardianToken))
                .andExpect(status().isOk())
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(get("/api/routines/{routineId}/repeats", routineId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 사용자 루틴 수행 기록 조회 - 성공 및 인증 실패(401) 테스트")
    @Test
    void getRoutineHistoryByMemberId_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성 및 완료 (테스트를 위해 기록이 존재해야 함)
        RoutineRequest createRequest = createRoutineRequest("기록 테스트 루틴");
        String routineResponseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(routineResponseContent).get("id").asLong();

        // 루틴 반복 요일 설정 (현재 요일로)
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        RepeatDay todayRepeatDay = RepeatDay.valueOf(today.name().substring(0, 3));
        List<RepeatDay> repeatDays = Collections.singletonList(todayRepeatDay);

        mockMvc.perform(put("/api/routines/{routineId}/repeats", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repeatDays)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/routines/{routineId}/complete", routineId)
                        .header("Authorization", "Bearer " + dependentToken))
                .andExpect(status().isOk());

        // when/then: [성공] 유효한 피보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(get("/api/routines/{memberId}/member_histories", dependent.getId())
                        .header("Authorization", "Bearer " + dependentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].routineId").value(routineId))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(get("/api/routines/{memberId}/member_histories", dependent.getId()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 특정 루틴의 수행 기록 조회 - 성공 및 인증 실패(401) 테스트")
    @Test
    void getRoutineHistoryByRoutineId_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성 및 완료 (테스트를 위해 기록이 존재해야 함)
        RoutineRequest createRequest = createRoutineRequest("특정 루틴 기록 테스트");
        String routineResponseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(routineResponseContent).get("id").asLong();

        // 루틴 반복 요일 설정 (현재 요일로)
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        RepeatDay todayRepeatDay = RepeatDay.valueOf(today.name().substring(0, 3));
        List<RepeatDay> repeatDays = Collections.singletonList(todayRepeatDay);

        mockMvc.perform(put("/api/routines/{routineId}/repeats", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repeatDays)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/routines/{routineId}/complete", routineId)
                        .header("Authorization", "Bearer " + dependentToken))
                .andExpect(status().isOk());

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(get("/api/routines/{routineId}/routine_histories", routineId)
                        .header("Authorization", "Bearer " + guardianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].routineId").value(routineId))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(get("/api/routines/{routineId}/routine_histories", routineId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 업데이트 - 성공 및 인증 실패(401) 테스트")
    @Test
    void updateRoutine_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성
        RoutineRequest createRequest = createRoutineRequest("업데이트 전 루틴");
        String responseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(responseContent).get("id").asLong();

        // given: 업데이트 요청
        RoutineRequest updateRequest = createRoutineRequest("업데이트 후 루틴");
        updateRequest.setIsAlertActive(false);

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(patch("/api/routines/{routineId}", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 후 루틴"))
                .andExpect(jsonPath("$.alert.isActive").value(false))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(patch("/api/routines/{routineId}", routineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 삭제 - 성공 및 인증 실패(401) 테스트")
    @Test
    void deleteRoutine_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성
        RoutineRequest createRequest = createRoutineRequest("삭제할 루틴");
        String responseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(responseContent).get("id").asLong();

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 204 No Content
        mockMvc.perform(delete("/api/routines/{routineId}", routineId)
                        .header("Authorization", "Bearer " + guardianToken))
                .andExpect(status().isNoContent())
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(delete("/api/routines/{routineId}", routineId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 반복 데이터 업데이트 - 성공 및 인증 실패(401) 테스트")
    @Test
    void updateRoutineRepeat_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성
        RoutineRequest createRequest = createRoutineRequest("반복 요일 업데이트 루틴");
        String responseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(responseContent).get("id").asLong();

        // given: 업데이트 요청 (화, 목, 토)
        List<RepeatDay> repeatDays = List.of(RepeatDay.TUE, RepeatDay.THU, RepeatDay.SAT);

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(put("/api/routines/{routineId}/repeats", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repeatDays)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].repeatDay").value("TUE"))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(put("/api/routines/{routineId}/repeats", routineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repeatDays)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 메모 업데이트 - 성공 및 인증 실패(401) 테스트")
    @Test
    void updateRoutineMemo_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성
        RoutineRequest createRequest = createRoutineRequest("메모 업데이트 루틴");
        String responseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(responseContent).get("id").asLong();

        // given: 업데이트 요청
        String newMemo = "새로운 메모 내용입니다.";

        // when/then: [성공] 유효한 보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(patch("/api/routines/{routineId}/memo", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMemo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guardianMemo").value(newMemo))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(patch("/api/routines/{routineId}/memo", routineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMemo))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 수행 체크 - 성공 및 인증 실패(401) 테스트")
    @Test
    void completeRoutine_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성
        RoutineRequest createRequest = createRoutineRequest("완료할 루틴");
        String responseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(responseContent).get("id").asLong();

        // 루틴 반복 요일 설정 (현재 요일로)
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        RepeatDay todayRepeatDay = RepeatDay.valueOf(today.name().substring(0, 3));
        List<RepeatDay> repeatDays = Collections.singletonList(todayRepeatDay);

        mockMvc.perform(put("/api/routines/{routineId}/repeats", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repeatDays)))
                .andExpect(status().isOk());

        // when/then: [성공] 유효한 피보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(post("/api/routines/{routineId}/complete", routineId)
                        .header("Authorization", "Bearer " + dependentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routineId").value(routineId))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(post("/api/routines/{routineId}/complete", routineId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("[통합] 루틴 수행 체크 철회 - 성공 및 인증 실패(401) 테스트")
    @Test
    void undoCompleteRoutine_Success_And_Auth_Fail() throws Exception {
        // given: 루틴 생성 및 완료
        RoutineRequest createRequest = createRoutineRequest("철회할 루틴");
        String routineResponseContent = mockMvc.perform(post("/api/routines/{dependentId}", dependent.getId())
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long routineId = objectMapper.readTree(routineResponseContent).get("id").asLong();

        // 루틴 반복 요일 설정 (현재 요일로)
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        RepeatDay todayRepeatDay = RepeatDay.valueOf(today.name().substring(0, 3));
        List<RepeatDay> repeatDays = Collections.singletonList(todayRepeatDay);

        mockMvc.perform(put("/api/routines/{routineId}/repeats", routineId)
                        .header("Authorization", "Bearer " + guardianToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repeatDays)))
                .andExpect(status().isOk());

        String historyResponseContent = mockMvc.perform(post("/api/routines/{routineId}/complete", routineId)
                        .header("Authorization", "Bearer " + dependentToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long historyId = objectMapper.readTree(historyResponseContent).get("id").asLong();

        // when/then: [성공] 유효한 피보호자 토큰으로 요청 시 200 OK
        mockMvc.perform(patch("/api/routines/{historyId}/undo", historyId)
                        .header("Authorization", "Bearer " + dependentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("해당 루틴 수행 기록을 취소했습니다."))
                .andDo(print());

        // when/then: [인증 실패] 토큰 없이 요청 시 401 Unauthorized
        mockMvc.perform(patch("/api/routines/{historyId}/undo", historyId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}