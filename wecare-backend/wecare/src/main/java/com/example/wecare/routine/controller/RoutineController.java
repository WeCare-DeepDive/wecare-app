package com.example.wecare.routine.controller;

import com.example.wecare.routine.code.RepeatDay;
import com.example.wecare.routine.dto.RoutineDto;
import com.example.wecare.routine.dto.RoutineHistoryDto;
import com.example.wecare.routine.dto.RoutineRepeatDayDto;
import com.example.wecare.routine.dto.RoutineRequest;
import com.example.wecare.routine.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@Tag(name = "루틴 서비스", description = "루틴 조회, 수정, 조작 등")
public class RoutineController {
    private final RoutineService routineService;

    @Operation(
            summary = "피보호자 루틴 목록 조회",
            description = "피보호자 본인 또는 연결된 보호자만 피보호자의 식별자를 통해 접근 가능",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{dependentId}")
    public ResponseEntity<List<RoutineDto>> getRoutinesByDependentId(
            @PathVariable Long dependentId
    ) {
        return ResponseEntity.ok(routineService.getRoutinesByDependentId(dependentId));
    }

    @Operation(
            summary = "루틴 반복 데이터 조회",
            description = "피보호자 본인 또는 연결된 보호자만 루틴의 식별자를 통해 접근 가능",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{routineId}/repeats")
    public ResponseEntity<List<RoutineRepeatDayDto>> getRepeatDaysByRoutineId(
            @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.getRepeatDaysByRoutineId(routineId));
    }

    @Operation(
            summary = "사용자 루틴 수행 기록 조회",
            description = "루틴의 성공 여부(COMPLETED, FAILED)와 수행 시간 조회 가능",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{memberId}/member_histories")
    public ResponseEntity<List<RoutineHistoryDto>> getRoutineHistoryByMemberId(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(routineService.getHistoriesByMemberId(memberId));
    }

    @Operation(
            summary = "특정 루틴의 수행 기록 조회",
            description = "루틴의 식별자를 통해 해당 루틴의 수행 기록 조회",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{routineId}/routine_histories")
    public ResponseEntity<List<RoutineHistoryDto>> getRoutineHistoryByRoutineId(
            @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.getHistoriesByRoutineId(routineId));
    }

    @Operation(
            summary = "루틴 생성",
            description = "보호자만 피보호자의 식별자를 통해 접근 가능",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping("/{dependentId}")
    public ResponseEntity<RoutineDto> createRoutine(
            @PathVariable Long dependentId,
            @RequestBody @Valid RoutineRequest request) {
        return ResponseEntity.ok(routineService.createRoutine(dependentId, request));
    }

    @Operation(
            summary = "루틴 업데이트",
            description = "피보호자 본인 또는 연결된 보호자만 피보호자의 식별자를 통해 접근 가능",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{routineId}")
    public ResponseEntity<RoutineDto> updateRoutine(
            @PathVariable Long routineId,
            @RequestBody @Valid RoutineRequest request) {
        RoutineDto response = routineService.updateRoutine(routineId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "루틴 삭제",
            description = "Hard-delete, 데이터가 완전히 사라지며, 수행 기록은 보존됨",
            security = @SecurityRequirement(name = "Authorization")
    )
    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(
            @PathVariable Long routineId
    ) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "루틴 반복 데이터 업데이트",
            description = "수정 사항을 원본에 덮어쓰기 방식으로 업데이트",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PutMapping("/{routineId}/repeats")
    public ResponseEntity<List<RoutineRepeatDayDto>> updateRoutineRepeat(
            @PathVariable Long routineId,
            @RequestBody List<RepeatDay> requests
    ) {
        return ResponseEntity.ok(routineService.updateRepeatDays(routineId, requests));
    }

    @Operation(
            summary = "루틴 메모 업데이트",
            description = "요청하는 사용자의 역할에 따라 메모를 업데이트",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{routineId}/memo")
    public ResponseEntity<RoutineDto> updateRoutineMemo(
            @PathVariable Long routineId,
            @RequestBody String content
    ) {
        return ResponseEntity.ok(routineService.updateRoutineMemo(routineId, content));
    }

    @Operation(
            summary = "루틴 수행 체크",
            description = "당일 시작시간과 종료시간 사이에 체크 가능하며, 종료시간이 정의되지 않았을 경우 " +
                    "당일 내로 체크 가능, 가능한 시간 외에 체크할 경우 실패로 처리 " +
                    "(23:59분에 매일 요청하여 당일 루틴 수행 여부 일괄적으로 체크 가능)",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping("/{routineId}/complete")
    public ResponseEntity<RoutineHistoryDto> completeRoutine(
            @PathVariable Long routineId
    ) {
        return ResponseEntity.ok(routineService.completeRoutine(routineId));
    }

    @Operation(
            summary = "루틴 수행 체크 철회",
            description = "체크 해제할 경우 수행 기록이 삭제됨, 체크 가능 내에만 철회 가능",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{historyId}/undo")
    public ResponseEntity<String> undoCompleteRoutine(
            @PathVariable Long historyId
    ) {
        routineService.undoCompleteRoutine(historyId);
        return ResponseEntity.ok("해당 루틴 수행 기록을 취소했습니다.");
    }
}