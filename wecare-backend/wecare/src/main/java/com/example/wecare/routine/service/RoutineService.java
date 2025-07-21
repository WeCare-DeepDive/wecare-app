package com.example.wecare.routine.service;

import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineAlarmSetting;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.dto.RoutineResponse;
import com.example.wecare.routine.repository.RoutineRepository;
import com.example.wecare.routine.repository.RoutineAlarmSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;
    private final RoutineAlarmSettingRepository routineAlarmSettingRepository;

    private Long getCurrentMemberId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private Member getCurrentMember() {
        Long currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public RoutineResponse createRoutine(Long dependentId, RoutineRequest request) {
        Member guardian = getCurrentMember();
        if (guardian.getRole() != Role.GUARDIAN) {
            throw new AccessDeniedException("루틴을 생성할 수 있는 권한이 없습니다. 보호자만 루틴을 생성할 수 있습니다.");
        }

        Member dependent = memberRepository.findById(dependentId)
                .orElseThrow(() -> new IllegalArgumentException("피보호자를 찾을 수 없습니다."));

        if (dependent.getRole() != Role.DEPENDENT) {
            throw new IllegalArgumentException("루틴 대상은 피보호자여야 합니다.");
        }

        if (dependent.getGuardian() == null || !dependent.getGuardian().getId().equals(guardian.getId())) {
            throw new AccessDeniedException("해당 피보호자에 대한 루틴을 생성할 권한이 없습니다.");
        }

        Routine routine = Routine.builder()
                .guardian(guardian)
                .dependent(dependent)
                .type(request.getType())
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .is_repeat(request.isRepeat())
                .repeatDays(request.getRepeatDays())
                
                
                .completed(false) // 생성 시점에는 항상 false
                .build();

        Routine savedRoutine = routineRepository.save(routine);

        // 알림 설정 저장
        if (request.getAlertBeforeStartMin() != null ||
            request.getAlertBeforeEndMin() != null ||
            request.getRepeatIntervalMin() != null) {
            RoutineAlarmSetting alarmSetting = RoutineAlarmSetting.builder()
                .routine(savedRoutine)
                .alertBeforeStartMin(request.getAlertBeforeStartMin())
                .alertBeforeEndMin(request.getAlertBeforeEndMin())
                .repeatIntervalMin(request.getRepeatIntervalMin())
                .build();
            routineAlarmSettingRepository.save(alarmSetting);
            savedRoutine.setAlarmSetting(alarmSetting); // 루틴 엔티티에 알림 설정 연결
        }

        return convertToResponse(savedRoutine);
    }

    public List<RoutineResponse> getRoutinesByGuardian() {
        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.GUARDIAN) {
            throw new AccessDeniedException("보호자만 루틴 목록을 조회할 수 있습니다.");
        }
        List<Routine> routines = routineRepository.findByGuardianId(currentMember.getId());
        return routines.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<RoutineResponse> getRoutinesByDependent() {
        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.DEPENDENT) {
            throw new AccessDeniedException("피보호자만 루틴 목록을 조회할 수 있습니다.");
        }
        List<Routine> routines = routineRepository.findByDependentId(currentMember.getId());
        return routines.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public RoutineResponse getRoutineById(Long routineId) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        return convertToResponse(routine);
    }

    @Transactional
    public RoutineResponse updateRoutine(Long routineId, RoutineRequest request) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        Member currentMember = getCurrentMember();

        if (!routine.getGuardian().getId().equals(currentMember.getId())) {
            throw new AccessDeniedException("루틴을 수정할 권한이 없습니다.");
        }

        routine.setType(request.getType());
        routine.setTitle(request.getTitle());
        routine.setDescription(request.getDescription());
        routine.setStartTime(request.getStartTime());
        routine.setEndTime(request.getEndTime());
        routine.set_repeat(request.isRepeat());
        routine.setRepeatDays(request.getRepeatDays());

        // 알림 설정 업데이트
        RoutineAlarmSetting existingAlarmSetting = routine.getAlarmSetting();
        boolean hasAlarmRequest = request.getAlertBeforeStartMin() != null ||
                                  request.getAlertBeforeEndMin() != null ||
                                  request.getRepeatIntervalMin() != null;

        if (hasAlarmRequest) {
            if (existingAlarmSetting == null) {
                // 새로운 알림 설정 생성
                RoutineAlarmSetting newAlarmSetting = RoutineAlarmSetting.builder()
                    .routine(routine)
                    .alertBeforeStartMin(request.getAlertBeforeStartMin())
                    .alertBeforeEndMin(request.getAlertBeforeEndMin())
                    .repeatIntervalMin(request.getRepeatIntervalMin())
                    .build();
                routine.setAlarmSetting(newAlarmSetting);
                routineAlarmSettingRepository.save(newAlarmSetting);
            } else {
                // 기존 알림 설정 업데이트
                existingAlarmSetting.setAlertBeforeStartMin(request.getAlertBeforeStartMin());
                existingAlarmSetting.setAlertBeforeEndMin(request.getAlertBeforeEndMin());
                existingAlarmSetting.setRepeatIntervalMin(request.getRepeatIntervalMin());
                routineAlarmSettingRepository.save(existingAlarmSetting);
            }
        } else if (existingAlarmSetting != null) {
            // 알림 설정이 요청에 없지만 기존에 존재하면 삭제
            routine.setAlarmSetting(null); // Routine 엔티티에서 연결 해제
            routineAlarmSettingRepository.delete(existingAlarmSetting);
        }

        Routine updatedRoutine = routineRepository.save(routine);
        return convertToResponse(updatedRoutine);
    }

    @Transactional
    public void deleteRoutine(Long routineId) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        Member currentMember = getCurrentMember();

        if (!routine.getGuardian().getId().equals(currentMember.getId())) {
            throw new AccessDeniedException("루틴을 삭제할 권한이 없습니다.");
        }
        routineRepository.delete(routine);
    }

    @Transactional
    public void completeRoutine(Long routineId) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        Member currentMember = getCurrentMember();

        if (currentMember.getRole() != Role.DEPENDENT) {
            throw new AccessDeniedException("루틴을 완료할 수 있는 권한이 없습니다. 피보호자만 가능합니다.");
        }

        if (!routine.getDependent().getId().equals(currentMember.getId())) {
            throw new AccessDeniedException("해당 루틴을 완료할 권한이 없습니다.");
        }

        routine.setCompleted(true);
        routineRepository.save(routine);
    }

    private Routine findRoutineByIdAndCheckAccess(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));
        Member currentMember = getCurrentMember();

        boolean isGuardian = routine.getGuardian().getId().equals(currentMember.getId());
        boolean isDependent = routine.getDependent().getId().equals(currentMember.getId());

        if (!isGuardian && !isDependent) {
            throw new AccessDeniedException("해당 루틴에 접근할 권한이 없습니다.");
        }
        return routine;
    }

    private RoutineResponse convertToResponse(Routine routine) {
        return RoutineResponse.builder()
                .id(routine.getId())
                .guardianId(routine.getGuardian().getId())
                .dependentId(routine.getDependent().getId())
                .type(routine.getType())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .startTime(routine.getStartTime())
                .endTime(routine.getEndTime())
                .repeat(routine.is_repeat())
                .repeatDays(routine.getRepeatDays())
                .alertBeforeStartMin(routine.getAlarmSetting() != null ? routine.getAlarmSetting().getAlertBeforeStartMin() : null)
                .alertBeforeEndMin(routine.getAlarmSetting() != null ? routine.getAlarmSetting().getAlertBeforeEndMin() : null)
                .repeatIntervalMin(routine.getAlarmSetting() != null ? routine.getAlarmSetting().getRepeatIntervalMin() : null)
                .completed(routine.isCompleted())
                .build();
    }
}