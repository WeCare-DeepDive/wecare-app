package com.example.wecare.routine.service;

import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.repository.RoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    @InjectMocks
    private RoutineService routineService;

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SecurityContext securityContext;

    private Member guardian; // 보호자
    private Member dependent; // 피보호자 (보호자와 연결됨)
    private Member otherMember; // 관계없는 제3자

    @BeforeEach
    void setUp() {
        // Mock SecurityContext 설정
        SecurityContextHolder.setContext(securityContext);

        // 테스트용 사용자 데이터 설정
        guardian = new Member();
        guardian.setId(1L);
        guardian.setRole(Role.GUARDIAN);

        dependent = new Member();
        dependent.setId(2L);
        dependent.setRole(Role.DEPENDENT);
        dependent.setGuardian(guardian); // 보호자와 피보호자 연결

        otherMember = new Member();
        otherMember.setId(3L);
        otherMember.setRole(Role.GUARDIAN); // 다른 보호자
    }

    private void mockCurrentUser(Member member) {
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(member.getId().toString(), null));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
    }

    @Test
    @DisplayName("성공: 보호자가 자신의 피보호자에게 루틴 생성")
    void createRoutine_Success() {
        // given
        mockCurrentUser(guardian);
        when(memberRepository.findById(dependent.getId())).thenReturn(Optional.of(dependent));
        when(routineRepository.save(any(Routine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoutineRequest request = new RoutineRequest(); // 필요한 필드 설정

        // when
        routineService.createRoutine(dependent.getId(), request);

        // then
        verify(routineRepository, times(1)).save(any(Routine.class));
    }

    @Test
    @DisplayName("실패: 피보호자가 루틴 생성을 시도")
    void createRoutine_Fail_WhenDependentTries() {
        // given
        mockCurrentUser(dependent);
        RoutineRequest request = new RoutineRequest();

        // when & then
        assertThatThrownBy(() -> routineService.createRoutine(dependent.getId(), request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("루틴을 생성할 수 있는 권한이 없습니다. 보호자만 루틴을 생성할 수 있습니다.");
    }

    @Test
    @DisplayName("실패: 보호자가 자신의 피보호자가 아닌 다른 사용자에게 루틴 생성 시도")
    void createRoutine_Fail_WhenNotMyDependent() {
        // given
        mockCurrentUser(guardian);
        Member anotherDependent = new Member(); // 이 피보호자는 다른 보호자와 연결됨
        anotherDependent.setId(4L);
        anotherDependent.setRole(Role.DEPENDENT);
        anotherDependent.setGuardian(otherMember);

        when(memberRepository.findById(anotherDependent.getId())).thenReturn(Optional.of(anotherDependent));

        RoutineRequest request = new RoutineRequest();

        // when & then
        assertThatThrownBy(() -> routineService.createRoutine(anotherDependent.getId(), request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("해당 피보호자에 대한 루틴을 생성할 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패: 제3자가 특정 루틴 정보 조회 시도")
    void getRoutineById_Fail_WhenAccessedByOthers() {
        // given
        mockCurrentUser(otherMember);
        Routine routine = Routine.builder().id(1L).guardian(guardian).dependent(dependent).build();
        when(routineRepository.findById(routine.getId())).thenReturn(Optional.of(routine));

        // when & then
        assertThatThrownBy(() -> routineService.getRoutineById(routine.getId()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("해당 루틴에 접근할 권한이 없습니다.");
    }

    @Test
    @DisplayName("실패: 피보호자가 루틴 삭제 시도")
    void deleteRoutine_Fail_WhenDependentTries() {
        // given
        mockCurrentUser(dependent);
        Routine routine = Routine.builder().id(1L).guardian(guardian).dependent(dependent).build();
        when(routineRepository.findById(routine.getId())).thenReturn(Optional.of(routine));

        // when & then
        assertThatThrownBy(() -> routineService.deleteRoutine(routine.getId()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("루틴을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("성공: 피보호자가 자신의 루틴을 완료 처리")
    void completeRoutine_Success() {
        // given
        mockCurrentUser(dependent);
        Routine routine = Routine.builder().id(1L).guardian(guardian).dependent(dependent).completed(false).build();
        when(routineRepository.findById(routine.getId())).thenReturn(Optional.of(routine));

        // when
        routineService.completeRoutine(routine.getId());

        // then
        ArgumentCaptor<Routine> routineCaptor = ArgumentCaptor.forClass(Routine.class);
        verify(routineRepository, times(1)).save(routineCaptor.capture());
        Routine savedRoutine = routineCaptor.getValue();
        assertThat(savedRoutine.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("실패: 보호자가 루틴 완료 처리 시도")
    void completeRoutine_Fail_WhenGuardianTries() {
        // given
        mockCurrentUser(guardian);
        Routine routine = Routine.builder().id(1L).guardian(guardian).dependent(dependent).build();
        when(routineRepository.findById(routine.getId())).thenReturn(Optional.of(routine));

        // when & then
        assertThatThrownBy(() -> routineService.completeRoutine(routine.getId()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("루틴을 완료할 수 있는 권한이 없습니다. 피보호자만 가능합니다.");
    }
}
