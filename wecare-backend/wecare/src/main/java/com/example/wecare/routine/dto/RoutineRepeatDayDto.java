package com.example.wecare.routine.dto;


import com.example.wecare.routine.code.RepeatDay;
import com.example.wecare.routine.domain.RoutineRepeatDay;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineRepeatDayDto {
    private Long id;
    private Long routineId;
    private RepeatDay repeatDay;

    public static RoutineRepeatDayDto fromEntity(RoutineRepeatDay repeatDay) {
        return RoutineRepeatDayDto.builder()
                .id(repeatDay.getId())
                .routineId(repeatDay.getRoutine().getId())
                .repeatDay(repeatDay.getRepeatDay())
                .build();
    }
}