package com.example.wecare.routine.controller;

import com.example.wecare.routine.code.RepeatDay;
import com.example.wecare.routine.dto.RoutineDto;
import com.example.wecare.routine.dto.RoutineHistoryDto;
import com.example.wecare.routine.dto.RoutineRepeatDayDto;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;

    @GetMapping("/{dependentId}")
    public ResponseEntity<List<RoutineDto>> getRoutinesByDependentId(
            @PathVariable Long dependentId
    ) {
        return ResponseEntity.ok(routineService.getRoutinesByDependentId(dependentId));
    }

    @GetMapping("/{routineId}")
    public ResponseEntity<List<RoutineRepeatDayDto>> getRepeatDaysByRoutineId(
            @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.getRepeatDaysByRoutineId(routineId));
    }

    @GetMapping("/{memberId}/member_histories")
    public ResponseEntity<List<RoutineHistoryDto>> getRoutineHistoryByMemberId(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(routineService.getHistoriesByMemberId(memberId));
    }

    @GetMapping("/{routineId}/routine_histories")
    public ResponseEntity<List<RoutineHistoryDto>> getRoutineHistoryByRoutineId(
            @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.getHistoriesByRoutineId(routineId));
    }

    @PostMapping("/{dependentId}")
    public ResponseEntity<RoutineDto> createRoutine(
            @PathVariable Long dependentId,
            @RequestBody RoutineRequest request) {
        return ResponseEntity.ok(routineService.createRoutine(dependentId, request));
    }

    @PatchMapping("/{routineId}")
    public ResponseEntity<RoutineDto> updateRoutine(
            @PathVariable Long routineId,
            @RequestBody RoutineRequest request) {
        RoutineDto response = routineService.updateRoutine(routineId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(
            @PathVariable Long routineId
    ) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{routineId}/repeat")
    public ResponseEntity<List<RoutineRepeatDayDto>> updateRoutineRepeat(
            @PathVariable Long routineId,
            @RequestBody List<RepeatDay> requests
    ) {
        return ResponseEntity.ok(routineService.updateRepeatDays(routineId, requests));
    }

    @PatchMapping("/{routineId}/memo")
    public ResponseEntity<RoutineDto> updateRoutineMemo(
            @PathVariable Long routineId,
            @RequestBody String content
    ) {
        return ResponseEntity.ok(routineService.updateRoutineMemo(routineId, content));
    }

    @PostMapping("/{routineId}/complete")
    public ResponseEntity<RoutineHistoryDto> completeRoutine(
            @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.completeRoutine(routineId));
    }

    @PatchMapping("/{historyId}/undo")
    public ResponseEntity<String> undoCompleteRoutine(
            @PathVariable Long historyId
    ) {
        routineService.undoCompleteRoutine(historyId);
        return ResponseEntity.ok("해당 루틴 수행 기록을 취소했습니다.");
    }
}