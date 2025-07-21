package com.example.wecare.routine.dto;


import com.example.wecare.routine.domain.RepeatDay;
import com.example.wecare.routine.domain.RoutineType;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineResponse {

    private Long id;
    private Long guardianId;
    private Long dependentId;
    private RoutineType type;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean repeat;
    private List<RepeatDay> repeatDays;
    private Integer alertBeforeStartMin;
    private Integer alertBeforeEndMin;
    private Integer repeatIntervalMin;
    private boolean completed;
}