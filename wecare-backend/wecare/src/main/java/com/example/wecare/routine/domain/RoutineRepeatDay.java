package com.example.wecare.routine.domain;

import com.example.wecare.routine.code.RepeatDay;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "routine_repeat_days")
public class RoutineRepeatDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne // getRoutine().getId() 참조하므로 EAGER
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_day")
    private RepeatDay repeatDay;
}
