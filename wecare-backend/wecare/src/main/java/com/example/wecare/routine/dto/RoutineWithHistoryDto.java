package com.example.wecare.routine.dto;

import com.example.wecare.routine.code.RoutineType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineWithHistoryDto {
    private Long id;
    private Long dependentId;
    private Long connectionId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private RoutineType routineType;
    private String guardianMemo;
    private String dependentMemo;
    private RoutineHistoryDto history;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoutineWithHistoryDto fromEntity(
            RoutineDto routineDto,
            RoutineHistoryDto history
    ) {
        return RoutineWithHistoryDto.builder()
                .id(routineDto.getId())
                .dependentId(routineDto.getDependentId())
                .startTime(routineDto.getStartTime())
                .endTime(routineDto.getEndTime())
                .title(routineDto.getTitle())
                .routineType(routineDto.getRoutineType())
                .guardianMemo(routineDto.getGuardianMemo())
                .dependentMemo(routineDto.getDependentMemo())
                .history(history)
                .createdAt(routineDto.getCreatedAt())
                .updatedAt(routineDto.getUpdatedAt())
                .build();
    }
}
