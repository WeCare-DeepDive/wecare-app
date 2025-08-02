package com.example.wecare.routine.dto;


import com.example.wecare.routine.code.RoutineType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDetailDto {
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
    private List<RoutineRepeatDayDto> repeats;
    private RoutineHistoryDto history;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static RoutineDetailDto fromRoutineDto(
            RoutineDto routineDto,
            List<RoutineRepeatDayDto> repeats,
            RoutineHistoryDto history
    ) {
        return RoutineDetailDto.builder()
                .id(routineDto.getId())
                .dependentId(routineDto.getDependentId())
                .startTime(routineDto.getStartTime())
                .endTime(routineDto.getEndTime())
                .title(routineDto.getTitle())
                .routineType(routineDto.getRoutineType())
                .guardianMemo(routineDto.getGuardianMemo())
                .dependentMemo(routineDto.getDependentMemo())
                .createdAt(routineDto.getCreatedAt())
                .updatedAt(routineDto.getUpdatedAt())
                .repeats(repeats)
                .history(history)
                .build();
    }
}
