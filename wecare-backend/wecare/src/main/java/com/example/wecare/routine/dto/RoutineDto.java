package com.example.wecare.routine.dto;


import com.example.wecare.routine.code.RoutineType;
import com.example.wecare.routine.domain.Routine;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDto {
    private Long id;
    private Long dependentId;
    private Long connectionId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private RoutineType routineType;
    private String guardianMemo;
    private String dependentMemo;
    private RoutineAlertDto alert;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoutineDto fromEntity(
            Routine routine,
            RoutineAlertDto alert
    ) {
        return RoutineDto.builder()
                .id(routine.getId())
                .dependentId(routine.getDependent().getId())
                .startTime(routine.getStartTime())
                .endTime(routine.getEndTime())
                .title(routine.getTitle())
                .routineType(routine.getRoutineType())
                .guardianMemo(routine.getGuardianMemo())
                .dependentMemo(routine.getDependentMemo())
                .alert(alert)
                .createdAt(routine.getCreatedAt())
                .updatedAt(routine.getUpdatedAt())
                .build();
    }
}