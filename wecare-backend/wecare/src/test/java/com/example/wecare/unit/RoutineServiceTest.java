package com.example.wecare.unit;

import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.code.NotificationType;
import com.example.wecare.routine.code.RepeatDay;
import com.example.wecare.routine.code.SoundType;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineAlert;
import com.example.wecare.routine.domain.RoutineHistory;
import com.example.wecare.routine.domain.RoutineRepeatDay;
import com.example.wecare.routine.dto.RoutineDto;
import com.example.wecare.routine.dto.RoutineHistoryDto;
import com.example.wecare.routine.dto.RoutineRepeatDayDto;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.repository.RoutineAlertRepository;
import com.example.wecare.routine.repository.RoutineHistoryRepository;
import com.example.wecare.routine.repository.RoutineRepeatDayRepository;
import com.example.wecare.routine.repository.RoutineRepository;
import com.example.wecare.routine.service.RoutineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoutineServiceTest {

    @InjectMocks
    private RoutineService routineService;

    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private RoutineAlertRepository routineAlertRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RoutineRepeatDayRepository routineRepeatDayRepository;
    @Mock
    private RoutineHistoryRepository routineHistoryRepository;

    private Member dependent;
    private Member guardian;

    @BeforeEach
    void setUp() {
        dependent = createMember(1L, Role.DEPENDENT);
        guardian = createMember(2L, Role.GUARDIAN);
        setAuthentication(guardian); // 기본 로그인 사용자는 보호자로 설정
    }

    // 헬퍼 메서드
    private void setAuthentication(Member member) {
        // Mockito.mock을 사용하여 Authentication 객체 생성
        Authentication authentication = Mockito.mock(Authentication.class);
        // getPrincipal() 호출 시 인자로 받은 member 객체를 반환하도록 설정
        lenient().when(authentication.getPrincipal()).thenReturn(member);
        // 필요시 getAuthorities()도 모킹 (현재는 비어있는 리스트 반환)
        lenient().when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

        // Mockito.mock을 사용하여 SecurityContext 객체 생성
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        // getAuthentication() 호출 시 위에서 생성한 authentication 객체를 반환하도록 설정
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        // SecurityContextHolder에 설정
        SecurityContextHolder.setContext(securityContext);
    }

    private Member createMember(Long id, Role role) {
        return Member.builder().id(id).role(role).build();
    }

    private Routine createRoutine(Long id, Member dependent, String title) {
        return Routine.builder()
                .id(id)
                .dependent(dependent)
                .title(title)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .build();
    }

    private RoutineRequest createRoutineRequest(String title, boolean isAlertActive) {
        RoutineRequest request = new RoutineRequest();
        request.setTitle(title);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(10, 0));
        request.setIsAlertActive(isAlertActive);
        // 실제 서비스 코드에 맞게 기본값 설정
        request.setNotificationType(NotificationType.NONE);
        request.setSoundType(SoundType.DEFAULT_SOUND);
        return request;
    }

    // Tests
    @DisplayName("루틴 생성(핵심 정보) 성공")
    @Test
    void createRoutine_Success() {
        // given: `RoutineRequest` DTO를 사용하여 루틴의 핵심 정보와 알림 설정을 요청
        RoutineRequest request = createRoutineRequest("아침 약 먹기", true);
        Routine newRoutine = createRoutine(1L, dependent, request.getTitle());

        given(memberRepository.findById(dependent.getId())).willReturn(Optional.of(dependent));
        given(routineRepository.save(any(Routine.class))).willReturn(newRoutine);
        given(routineAlertRepository.save(any(RoutineAlert.class))).willAnswer(inv -> inv.getArgument(0));

        // when: 서비스를 호출하여 루틴을 생성하고, `RoutineDto`를 반환받음
        RoutineDto resultDto = routineService.createRoutine(dependent.getId(), request);

        // then: 반환된 `RoutineDto`가 요청한 `RoutineRequest`의 값을 잘 반영하는지 검증
        assertThat(resultDto.getTitle()).isEqualTo(request.getTitle());
        verify(routineRepository).save(any(Routine.class));
        verify(routineAlertRepository).save(any(RoutineAlert.class));
    }

    @DisplayName("루틴 수정(핵심 정보) 성공")
    @Test
    void updateRoutine_Success() {
        // given: `RoutineRequest` DTO를 사용하여 루틴 정보 수정을 요청
        Routine existingRoutine = createRoutine(1L, dependent, "기존 루틴");
        RoutineAlert existingAlert = RoutineAlert.builder().routine(existingRoutine).isActive(true).build();
        RoutineRequest request = createRoutineRequest("수정된 루틴", false);

        given(routineRepository.findById(existingRoutine.getId())).willReturn(Optional.of(existingRoutine));
        given(routineAlertRepository.findByRoutine(existingRoutine)).willReturn(Optional.of(existingAlert));
        given(routineRepository.save(any(Routine.class))).willAnswer(inv -> inv.getArgument(0));
        given(routineAlertRepository.save(any(RoutineAlert.class))).willAnswer(inv -> inv.getArgument(0));

        // when: 서비스를 호출하여 루틴을 수정하고, `RoutineDto`를 반환받음
        RoutineDto resultDto = routineService.updateRoutine(existingRoutine.getId(), request);

        // then: 반환된 `RoutineDto`가 수정 요청(`RoutineRequest`)을 잘 반영하는지 검증
        assertThat(resultDto.getTitle()).isEqualTo("수정된 루틴");
        verify(routineRepository, times(2)).save(any(Routine.class));
        verify(routineAlertRepository).save(any(RoutineAlert.class));
    }

    @DisplayName("루틴 반복 요일 수정 성공")
    @Test
    void updateRepeatDays_Success() {
        // given: `List<RepeatDay>`를 직접 사용하여 반복 요일 수정을 요청
        Routine routine = createRoutine(1L, dependent, "요일 수정할 루틴");
        List<RepeatDay> newRepeatDays = List.of(RepeatDay.TUE, RepeatDay.FRI);

        given(routineRepository.findById(routine.getId())).willReturn(Optional.of(routine));
        given(routineRepeatDayRepository.saveAll(anyList())).willAnswer(inv -> inv.getArgument(0));

        // when: 서비스를 호출하여 반복 요일을 수정하고, `List<RoutineRepeatDayDto>`를 반환받음
        List<RoutineRepeatDayDto> resultDtos = routineService.updateRepeatDays(routine.getId(), newRepeatDays);

        // then: 반환된 `RoutineRepeatDayDto` 목록이 요청한 요일들을 잘 반영하는지 검증
        assertThat(resultDtos).hasSize(2);
        assertThat(resultDtos.stream().map(RoutineRepeatDayDto::getRepeatDay).toList()).contains(RepeatDay.TUE, RepeatDay.FRI);
        verify(routineRepeatDayRepository).deleteAllByRoutine(routine);
        verify(routineRepeatDayRepository).saveAll(anyList());
    }

    @DisplayName("루틴 완료 처리 성공")
    @Test
    void completeRoutine_Success() {
        // given: 피보호자가 특정 루틴을 완료했음을 알림 (입력값은 routineId)
        setAuthentication(dependent); // 피보호자로 로그인
        Routine routine = createRoutine(1L, dependent, "완료할 루틴");
        routine.setStartTime(LocalTime.now().minusHours(1));

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        RepeatDay todayRepeatDay = RepeatDay.valueOf(today.name().substring(0, 3));

        given(routineRepository.findById(routine.getId())).willReturn(Optional.of(routine));
        given(routineHistoryRepository.findByRoutineAndCompletedDate(any(), any())).willReturn(Optional.empty());
        given(routineRepeatDayRepository.findAllByRoutine(any())).willReturn(List.of(RoutineRepeatDay.builder().repeatDay(todayRepeatDay).build()));
        given(routineHistoryRepository.save(any(RoutineHistory.class))).willAnswer(inv -> {
            RoutineHistory history = inv.getArgument(0);
            history.setId(100L);
            return history;
        });

        // when: 서비스를 호출하여 루틴을 완료 처리하고, `RoutineHistoryDto`를 반환받음
        RoutineHistoryDto resultDto = routineService.completeRoutine(routine.getId());

        // then: 반환된 `RoutineHistoryDto`가 올바른 완료 기록을 담고 있는지 검증
        assertThat(resultDto.getRoutineId()).isEqualTo(routine.getId());
        assertThat(resultDto.getCompletedDate()).isEqualTo(LocalDate.now());
        verify(routineHistoryRepository).save(any(RoutineHistory.class));
    }

    // Other tests (delete, get, etc.) remain unchanged as they are already clear.
    @DisplayName("루틴 삭제 성공")
    @Test
    void deleteRoutine_Success() {
        // given
        Routine routineToDelete = createRoutine(1L, dependent, "삭제할 루틴");
        given(routineRepository.findById(routineToDelete.getId())).willReturn(Optional.of(routineToDelete));

        // when
        routineService.deleteRoutine(routineToDelete.getId());

        // then
        verify(routineRepository).delete(routineToDelete);
    }
}