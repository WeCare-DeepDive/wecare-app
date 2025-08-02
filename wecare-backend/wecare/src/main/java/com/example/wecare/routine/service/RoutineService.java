package com.example.wecare.routine.service;

import com.example.wecare.common.code.AuthResponseCode;
import com.example.wecare.common.code.GeneralResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.routine.code.RepeatDay;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineAlert;
import com.example.wecare.routine.domain.RoutineHistory;
import com.example.wecare.routine.domain.RoutineRepeatDay;
import com.example.wecare.routine.dto.*;
import com.example.wecare.routine.repository.RoutineAlertRepository;
import com.example.wecare.routine.repository.RoutineHistoryRepository;
import com.example.wecare.routine.repository.RoutineRepeatDayRepository;
import com.example.wecare.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineRepeatDayRepository routineRepeatDayRepository;
    private final RoutineAlertRepository routineAlertRepository;
    private final RoutineHistoryRepository routineHistoryRepository;
    private final MemberRepository memberRepository;

    //@PreAuthorize("@dependentAccessHandler.ownershipCheck(#dependentId)")
    @Transactional(readOnly = true)
    public List<RoutineWithHistoryDto> getRoutinesWithHistoryByDependentIdAndDate(Long dependentId, LocalDate date) {

        Member dependent = memberRepository.findById(dependentId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        if (dependent.getRole() != Role.DEPENDENT) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "피보호자의 루틴에만 접근할 수 있습니다.");
        }

        // 루틴과 루틴 알람 조합하여 DTO 변환하여 반환
        List<Routine> routines = routineRepository.findAllByDependent(dependent);
        List<RoutineWithHistoryDto> dtos = new ArrayList<>();
        for (Routine routine : routines) {
            RoutineHistory history = routineHistoryRepository.findByRoutineAndCompletedDate(routine, date)
                    .orElse(null);
            dtos.add(
                    RoutineWithHistoryDto.fromEntity(RoutineDto.fromEntity(routine), RoutineHistoryDto.fromEntity(history))
            );
        }

        return dtos;
    }

    //@PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    @Transactional(readOnly = true)
    public RoutineDetailDto getRoutineDetailByIdAndDate(Long routineId, LocalDate date) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        List<RoutineRepeatDay> repeats = routineRepeatDayRepository.findAllByRoutine(routine);
        RoutineHistory history = routineHistoryRepository.findByRoutineAndCompletedDate(routine, date)
                .orElse(null);

        return RoutineDetailDto.fromRoutineDto(
                RoutineDto.fromEntity(routine),
                repeats.stream().map(RoutineRepeatDayDto::fromEntity).toList(),
                RoutineHistoryDto.fromEntity(history)
        );
    }

    @Transactional
    @PreAuthorize("@dependentAccessHandler.ownershipCheck(#dependentId)")
    public RoutineDto createRoutine(Long dependentId, RoutineRequest request) {
        Member dependent = memberRepository.findById(dependentId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.DEPENDENT_NOT_FOUND));

        if (dependent.getRole() != Role.DEPENDENT) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "루틴 등록 대상은 피보호자여야 합니다.");
        }

        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.GUARDIAN) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "루틴 등록은 보호자만 가능합니다.");
        }

        validateRoutineRequest(request);

        Routine routine = Routine.builder()
                .dependent(dependent)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .title(request.getTitle())
                .routineType(request.getRoutineType())
                .guardianMemo(request.getGuardianMemo())
                .dependentMemo(request.getDependentMemo())
                .build();

        Routine savedRoutine = routineRepository.save(routine);
        List<RoutineRepeatDay> routineRepeatDays = new ArrayList<>();

        for (RepeatDay repeatDay : request.getRepeatDays()) {
            routineRepeatDays.add(RoutineRepeatDay.builder()
                    .routine(routine)
                    .repeatDay(repeatDay)
                    .build());
        }

        routineRepeatDayRepository.saveAll(routineRepeatDays);

        RoutineAlert routineAlert = RoutineAlert.builder()
                .routine(savedRoutine)
                .isActive(request.getIsAlertActive())
                .notificationType(request.getNotificationType())
                .soundType(request.getSoundType())
                .build();

        routineAlertRepository.save(routineAlert);

        return RoutineDto.fromEntity(savedRoutine);
    }

    @Transactional
    @PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    public RoutineDto updateRoutine(Long routineId, RoutineRequest request) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        RoutineAlert alert = routineAlertRepository.findByRoutine(routine)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.INTERNAL_SERVER_ERROR, "알람 정보를 찾을 수 없습니다."));

        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.GUARDIAN) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "루틴 수정은 보호자만 가능합니다.");
        }

        validateRoutineRequest(request);

        // 루틴 정보 업데이트
        routine.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) {
            routine.setEndTime(request.getEndTime());
        }

        routine.setTitle(request.getTitle());
        routine.setRoutineType(request.getRoutineType());
        if (request.getGuardianMemo() != null) {
            routine.setGuardianMemo(request.getGuardianMemo());
        }
        if (request.getDependentMemo() != null) {
            routine.setDependentMemo(request.getDependentMemo());
        }

        routine = routineRepository.save(routine);

        // 알람 관련 정보 업데이트
        if (request.getIsAlertActive() != null) {
            alert.setIsActive(request.getIsAlertActive());
        }
        alert.setNotificationType(request.getNotificationType());
        alert.setSoundType(request.getSoundType());

        routineAlertRepository.save(alert);

        Routine updatedRoutine = routineRepository.save(routine);

        return RoutineDto.fromEntity(updatedRoutine);
    }

    @PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    @Transactional
    public void deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.GUARDIAN) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "루틴 수정은 보호자만 가능합니다.");
        }

        routineRepository.delete(routine);
    }

    @PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    @Transactional(readOnly = true)
    public List<RoutineRepeatDayDto> getRepeatDaysByRoutineId(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        List<RoutineRepeatDay> repeatDays = routineRepeatDayRepository.findAllByRoutine(routine);

        return repeatDays.stream().map(RoutineRepeatDayDto::fromEntity).toList();
    }

    @PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    @Transactional
    public List<RoutineRepeatDayDto> updateRepeatDays(
            Long routineId, List<RepeatDay> repeatDayRequests
    ) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        if (repeatDayRequests == null || repeatDayRequests.isEmpty()) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "반복 형태가 비어있습니다.");
        }

        if (repeatDayRequests.size() != new HashSet<>(repeatDayRequests).size()) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "반복 형태 목록에 중복되는 값이 있습니다.");
        }

        routineRepeatDayRepository.deleteAllByRoutine(routine);

        List<RoutineRepeatDay> savedRepeatDays = new ArrayList<>();
        for (RepeatDay repeatDay : repeatDayRequests) {
            savedRepeatDays.add(RoutineRepeatDay.builder()
                    .repeatDay(repeatDay)
                    .routine(routine)
                    .build());
        }
        savedRepeatDays = routineRepeatDayRepository.saveAll(savedRepeatDays);

        return savedRepeatDays.stream().map(RoutineRepeatDayDto::fromEntity).toList();
    }

    @PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    @Transactional
    public RoutineDto updateRoutineMemo(Long routineId, String content) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        Member currentMember = getCurrentMember();

        if (currentMember.getRole() == Role.GUARDIAN) {
            routine.setGuardianMemo(content);
        } else if (currentMember.getRole() == Role.DEPENDENT) {
            routine.setDependentMemo(content);
        }

        routine = routineRepository.save(routine);

        routineAlertRepository.findByRoutine(routine)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        return RoutineDto.fromEntity(routine);
    }

    @PreAuthorize("@routineAccessHandler.ownershipCheck(#routineId)")
    @Transactional
    public RoutineHistoryDto completeRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = (Member) auth.getPrincipal();

        if (currentMember.getRole() != Role.DEPENDENT) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "피보호자만 루틴의 수행 여부를 조작할 수 있습니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        // 루틴 완료 가능한지 검증
        validateRoutineCompleting(routine);

        RoutineHistory todayHistory = RoutineHistory.builder()
                .routine(routine)
                .completedDate(LocalDate.now())
                .completedTime(LocalTime.now())
                .build();

        // 루틴 시간이 지났는지 확인 (종료 시간이 설정된 경우에만)
        /*
        if (routine.getEndTime() != null && now.toLocalTime().isAfter(routine.getEndTime())) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "현재 루틴 수행 기록을 수정할 수 없습니다.");
        }*/

        todayHistory = routineHistoryRepository.save(todayHistory);

        // 로그에 해당 루틴 완료 기록 저장 및 반환
        return RoutineHistoryDto.fromEntity(todayHistory);
    }

    @Transactional
    public void undoCompleteRoutine(Long historyId) {
        LocalDateTime now = LocalDateTime.now();
        RoutineHistory history = routineHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.HISTORY_NOT_FOUND));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = (Member) auth.getPrincipal();

        if (currentMember.getRole() != Role.DEPENDENT) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "피보호자만 루틴의 수행 여부를 조작할 수 있습니다.");
        }

        Routine routine = history.getRoutine();

        // 오늘이 루틴에 해당하는 요일인지 확인
        List<RoutineRepeatDay> repeatDays = routineRepeatDayRepository.findAllByRoutine(routine);
        if (repeatDays.stream()
                .map(r -> r.getRepeatDay().getDayOfWeek())
                .noneMatch(day -> day == now.getDayOfWeek())
                || now.toLocalTime().isBefore(routine.getStartTime())
                || routine.getEndTime() != null && now.toLocalTime().isAfter(routine.getEndTime())
        ) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "현재 루틴 수행 기록을 수정할 수 없습니다.");
        }

        routineHistoryRepository.delete(history);
    }

    private void validateRoutineRequest(RoutineRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "종료 시간은 시작 시간보다 이전일 수 없습니다.");
        }
    }

    private void validateRoutineCompleting(Routine routine) {
        LocalDateTime now = LocalDateTime.now();
        // 오늘 해당 루틴이 완료되었는지 확인
        routineHistoryRepository.findByRoutineAndCompletedDate(routine, now.toLocalDate())
                .ifPresent((h) -> {
                    throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "이미 오늘 완료된 루틴입니다.");
                });

        // 오늘이 루틴에 해당하는 요일인지 확인
        List<RoutineRepeatDay> repeatDays = routineRepeatDayRepository.findAllByRoutine(routine);
        /*
        if (repeatDays.stream()
                .map(r -> r.getRepeatDay().getDayOfWeek())
                .noneMatch(day -> day == now.getDayOfWeek())) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "현재 시간은 반복 요일에 해당하는 요일이 아닙니다.");
        }*/

        // 루틴 시작 시간 이전에는 완료 처리할 수 없도록 막기
        if (now.toLocalTime().isBefore(routine.getStartTime())) {
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "루틴 시작 시간 이전에는 완료 처리할 수 없습니다.");
        }
    }

    private Member getCurrentMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Member) auth.getPrincipal();
    }
}