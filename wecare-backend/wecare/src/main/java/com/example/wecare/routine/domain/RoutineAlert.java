package com.example.wecare.routine.domain;

import com.example.wecare.routine.code.NotificationType;
import com.example.wecare.routine.code.SoundType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "routine_alerts")
public class RoutineAlert {
    @Id
    private Long routineId;
    @MapsId
    @OneToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sound_type")
    private SoundType soundType;
}
