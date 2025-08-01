package com.example.wecare.routine.dto;


import com.example.wecare.routine.code.NotificationType;
import com.example.wecare.routine.code.SoundType;
import com.example.wecare.routine.domain.RoutineAlert;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineAlertDto {
    private Long routineId;
    private Boolean isActive;
    private NotificationType notificationType;
    private SoundType soundType;

    public static RoutineAlertDto fromEntity(RoutineAlert alert) {
        if(alert == null){
            return null;
        }

        return RoutineAlertDto.builder()
                .routineId(alert.getRoutineId())
                .isActive(alert.getIsActive())
                .notificationType(alert.getNotificationType())
                .soundType(alert.getSoundType())
                .build();
    }
}