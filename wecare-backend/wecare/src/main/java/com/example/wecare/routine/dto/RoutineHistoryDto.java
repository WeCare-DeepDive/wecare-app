package com.example.wecare.routine.dto;

import com.example.wecare.routine.code.HistoryStatus;
import com.example.wecare.routine.domain.RoutineHistory;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineHistoryDto {
    private Long id;
    private Long routineId;
    private HistoryStatus status;

    private LocalDate completedDate;
    private LocalTime completedTime;

    public static RoutineHistoryDto fromEntity(RoutineHistory routineHistory) {
        return RoutineHistoryDto.builder()
                .id(routineHistory.getId())
                .routineId(routineHistory.getRoutine().getId())
                .completedDate(routineHistory.getCompletedDate())
                .completedTime(routineHistory.getCompletedTime())
                .build();
    }
}
