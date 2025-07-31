package com.example.wecare.routine.domain;

import com.example.wecare.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routine", uniqueConstraints = @UniqueConstraint(name = "pk_routine", columnNames = {"id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_id", nullable = false, foreignKey = @ForeignKey(name = "fk_routine_guardian_id"))
    private Member guardian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_id", nullable = false, foreignKey = @ForeignKey(name = "fk_routine_dependent_id"))
    private Member dependent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoutineType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(name = "is_repeat", nullable = false)
    private boolean repeat;

    @ElementCollection(targetClass = RepeatDay.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "routine_repeat_days",
            joinColumns = @JoinColumn(name = "routine_id", foreignKey = @ForeignKey(name = "fk_routine_repeat_days_routine_id")),
            uniqueConstraints = @UniqueConstraint(name = "pk_routine_repeat_days", columnNames = {"routine_id", "day"}))
    @Column(name = "day", nullable = false)
    private List<RepeatDay> repeatDays;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sound_type")
    private SoundType soundType;

    @Column(name = "voice_message_url", length = 500)
    private String voiceMessageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "guardian_memo", columnDefinition = "TEXT")
    private String guardianMemo; // 보호자 메모

    @Column(name = "dependent_memo", columnDefinition = "TEXT")
    private String dependentMemo; // 피보호자 메모
}