package com.example.wecare.routine.dto;


import com.example.wecare.routine.code.NotificationType;
import com.example.wecare.routine.code.RoutineType;
import com.example.wecare.routine.code.SoundType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineRequest {
    @NotNull(message = "시작 시간은 필수입니다.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "루틴 타입은 필수입니다.")
    private RoutineType routineType;
    private Boolean isAlertActive;
    @NotNull(message = "알람의 형태는 필수입니다.")
    private NotificationType notificationType;
    @NotNull(message = "알람 사운드의 형태는 필수입니다.")
    private SoundType soundType;

    private String guardianMemo;
    private String dependentMemo;
}