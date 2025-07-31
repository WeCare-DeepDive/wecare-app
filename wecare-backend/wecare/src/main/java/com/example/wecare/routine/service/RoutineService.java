package com.example.wecare.routine.service;

import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.domain.NotificationType;
import com.example.wecare.routine.domain.RepeatDay;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.dto.RoutineResponse;
import com.example.wecare.routine.dto.RoutineMemoRequest;
import com.example.wecare.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;

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
        if (request.isRepeat() && (request.getRepeatDays() == null || request.getRepeatDays().isEmpty())) {
            throw new IllegalArgumentException("반복 루틴은 반복 요일이 지정되어야 합니다.");
        }
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 이전일 수 없습니다.");
        }
        Member guardian = getCurrentMember();

        if (guardian.getRole() != Role.GUARDIAN) {
            throw new AccessDeniedException("루틴을 생성할 수 있는 권한이 없습니다. 보호자만 루틴을 생성할 수 있습니다.");
        }

        Member dependent = memberRepository.findById(dependentId)
                .orElseThrow(() -> new IllegalArgumentException("피보호자를 찾을 수 없습니다."));

        if (dependent.getRole() != Role.DEPENDENT) {
            throw new IllegalArgumentException("루틴 대상은 피보호자여야 합니다.");
        }

        boolean isConnected = guardian.getDependentConnections().stream()
                .anyMatch(conn -> conn.getDependent().getId().equals(dependentId) && conn.isActive());

        if (!isConnected) {
            throw new AccessDeniedException("해당 피보호자에 대한 루틴을 생성할 권한이 없습니다. 활성화된 연결이 필요합니다.");
        }

        Routine routine = Routine.builder()
                .guardian(guardian)
                .dependent(dependent)
                .type(request.getType())
                .title(request.getTitle())
                .guardianMemo(request.getGuardianMemo())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .repeat(request.isRepeat())
                .repeatDays(request.getRepeatDays())
                .isEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : false)
                .notificationType(request.getNotificationType())
                .soundType(request.getSoundType())
                .voiceMessageUrl(request.getVoiceMessageUrl())
                .completedAt(null)
                .guardianMemo(request.getGuardianMemo()) // 보호자 메모 추가
                .dependentMemo(request.getDependentMemo()) // 피보호자 메모 추가
                .build();

        Routine savedRoutine = routineRepository.save(routine);

        return convertToResponse(savedRoutine);
    }

    public List<RoutineResponse> getRoutinesByGuardian() {
        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.GUARDIAN) {
            throw new AccessDeniedException("루틴을 생성할 수 있는 권한이 없습니다. 보호자만 루틴을 생성할 수 있습니다.");
        }
        List<Routine> routines = routineRepository.findByGuardianId(currentMember.getId());
        return routines.stream()
                .filter(routine -> currentMember.getDependentConnections().stream()
                        .anyMatch(conn -> conn.getDependent().getId().equals(routine.getDependent().getId()) && conn.isActive())) // isActive() 검증 추가
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
                .filter(routine -> currentMember.getGuardianConnections().stream()
                        .anyMatch(conn -> conn.getGuardian().getId().equals(routine.getGuardian().getId()) && conn.isActive())) // isActive() 검증 추가
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public RoutineResponse getRoutineById(Long routineId) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        return convertToResponse(routine);
    }

    @Transactional
    public RoutineResponse updateRoutine(Long routineId, RoutineRequest request) {
        if (request.isRepeat() && (request.getRepeatDays() == null || request.getRepeatDays().isEmpty())) {
            throw new IllegalArgumentException("반복 루틴은 반복 요일이 지정되어야 합니다.");
        }
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 이전일 수 없습니다.");
        }
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        Member currentMember = getCurrentMember();

        if (currentMember.getRole() != Role.GUARDIAN || !routine.getGuardian().getId().equals(currentMember.getId())) {
            throw new AccessDeniedException("루틴을 수정할 권한이 없습니다. 보호자만 루틴을 수정할 수 있습니다.");
        }

        routine.setType(request.getType());
        routine.setTitle(request.getTitle());
        if (request.getGuardianMemo() != null) {
            routine.setGuardianMemo(request.getGuardianMemo());
        }
        if (request.getDependentMemo() != null) {
            routine.setDependentMemo(request.getDependentMemo());
        }
        routine.setStartTime(request.getStartTime());
        routine.setEndTime(request.getEndTime());
        routine.setRepeat(request.isRepeat());
        if (request.getRepeatDays() != null && request.getRepeatDays().contains(RepeatDay.DAILY)) {
            routine.setRepeatDays(java.util.EnumSet.allOf(RepeatDay.class).stream()
                    .filter(day -> day != RepeatDay.DAILY) // DAILY는 실제 요일이 아니므로 제외
                    .collect(Collectors.toList()));
        } else if (request.getRepeatDays() != null) {
            routine.setRepeatDays(request.getRepeatDays());
        }

        routine.setRepeatDays(request.getRepeatDays());

        routine.setEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : false);
        routine.setNotificationType(request.getNotificationType());
        routine.setSoundType(request.getSoundType());
        routine.setVoiceMessageUrl(request.getVoiceMessageUrl());

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

        // 루틴 시작 시간 이전에는 완료 처리할 수 없도록 막기
        LocalDateTime now = LocalDateTime.now();
        if (routine.getStartTime() == null) {
            throw new IllegalArgumentException("루틴 시작 시간이 설정되지 않아 완료 처리할 수 없습니다.");
        }
        if (now.isBefore(routine.getStartTime())) {
            throw new IllegalArgumentException("루틴 시작 시간 이전에는 완료 처리할 수 없습니다.");
        }
        // 루틴 시간이 지났는지 확인 (종료 시간이 설정된 경우에만)
        if (routine.getEndTime() != null && now.isAfter(routine.getEndTime())) {
            throw new IllegalArgumentException("루틴 시간이 지나 완료 처리할 수 없습니다.");
        }

        routine.setCompletedAt(now); // completed 필드 대신 completedAt 필드를 현재 시간으로 설정
        routineRepository.save(routine);
    }

    @Transactional
    public void uncompleteRoutine(Long routineId) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        Member currentMember = getCurrentMember();

        if (currentMember.getRole() != Role.DEPENDENT) {
            throw new AccessDeniedException("루틴 완료를 해제할 수 있는 권한이 없습니다. 피보호자만 가능합니다.");
        }

        if (!routine.getDependent().getId().equals(currentMember.getId())) {
            throw new AccessDeniedException("해당 루틴의 완료를 해제할 권한이 없습니다.");
        }

        // 루틴 시간이 지났는지 확인
        LocalDateTime now = LocalDateTime.now();
        if (routine.getEndTime() != null && now.isAfter(routine.getEndTime())) {
            throw new IllegalArgumentException("루틴 시간이 지나 완료 해제할 수 없습니다.");
        }

        routine.setCompletedAt(null); // completedAt 필드를 null로 설정하여 완료 해제
        routineRepository.save(routine);
    }

    @Transactional
    public RoutineResponse updateRoutineMemo(Long routineId, RoutineMemoRequest request) {
        Routine routine = findRoutineByIdAndCheckAccess(routineId);
        Member currentMember = getCurrentMember();

        if (currentMember.getRole() == Role.GUARDIAN) {
            if (!routine.getGuardian().getId().equals(currentMember.getId())) {
                throw new AccessDeniedException("해당 루틴의 보호자 메모를 수정할 권한이 없습니다.");
            }
            if (request.getDependentMemo() != null) {
                throw new AccessDeniedException("보호자는 피보호자 메모를 수정할 수 없습니다.");
            }
            routine.setGuardianMemo(request.getGuardianMemo());
        } else if (currentMember.getRole() == Role.DEPENDENT) {
            if (!routine.getDependent().getId().equals(currentMember.getId())) {
                throw new AccessDeniedException("해당 루틴의 피보호자 메모를 수정할 권한이 없습니다.");
            }
            if (request.getGuardianMemo() != null) {
                throw new AccessDeniedException("피보호자는 보호자 메모를 수정할 수 없습니다.");
            }
            routine.setDependentMemo(request.getDependentMemo());
        } else {
            throw new AccessDeniedException("메모를 수정할 권한이 없습니다.");
        }

        Routine updatedRoutine = routineRepository.save(routine);
        return convertToResponse(updatedRoutine);
    }

    private Routine findRoutineByIdAndCheckAccess(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));
        Member currentMember = getCurrentMember();

        boolean isGuardian = currentMember.getRole() == Role.GUARDIAN &&
                currentMember.getDependentConnections().stream()
                        .anyMatch(conn -> conn.getDependent().getId().equals(routine.getDependent().getId()) && conn.isActive());

        boolean isDependent = currentMember.getRole() == Role.DEPENDENT &&
                currentMember.getGuardianConnections().stream()
                        .anyMatch(conn -> conn.getGuardian().getId().equals(routine.getGuardian().getId()) && conn.isActive());

        if (!isGuardian && !isDependent) {
            throw new AccessDeniedException("해당 루틴에 접근할 권한이 없습니다. 활성화된 연결이 필요합니다.");
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
                .guardianMemo(routine.getGuardianMemo())
                .startTime(routine.getStartTime())
                .endTime(routine.getEndTime())
                .repeat(routine.isRepeat())
                .repeatDays(routine.getRepeatDays())
                .isEnabled(routine.isEnabled())
                .notificationType(routine.getNotificationType())
                .soundType(routine.getSoundType())
                .voiceMessageUrl(routine.getVoiceMessageUrl())
                .completedAt(routine.getCompletedAt())
                .guardianMemo(routine.getGuardianMemo()) // 보호자 메모 추가
                .dependentMemo(routine.getDependentMemo()) // 피보호자 메모 추가
                .guardianName(routine.getGuardian().getName()) // 보호자 이름 추가
                .dependentName(routine.getDependent().getName()) // 피보호자 이름 추가
                .build();
    }
}