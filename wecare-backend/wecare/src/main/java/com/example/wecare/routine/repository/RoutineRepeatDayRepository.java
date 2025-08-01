package com.example.wecare.routine.repository;

import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineRepeatDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepeatDayRepository extends JpaRepository<RoutineRepeatDay, Long> {
    List<RoutineRepeatDay> findAllByRoutine(Routine routine);

    void deleteAllByRoutine(Routine routine);
}