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
    @JoinColumn(name = "guardian_id", nullable = false, foreignKey = @ForeignKey(name = "fk_routine_members_guardian_id"))
    private Member guardian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_id", nullable = false, foreignKey = @ForeignKey(name = "fk_routine_members_dependent_id"))
    private Member dependent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoutineType type;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean is_repeat;

    @ElementCollection(targetClass = RepeatDay.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "routine_repeat_days", joinColumns = @JoinColumn(name = "routine_id", foreignKey = @ForeignKey(name = "fk_routine_repeat_days_routine_routine_id")))
    @Column(name = "day", nullable = false)
    private List<RepeatDay> repeatDays;

    @OneToOne(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private RoutineAlarmSetting alarmSetting;

    private LocalDateTime createdAt;
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

    @Column(nullable = false)
    private boolean completed;
}