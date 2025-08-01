package com.example.wecare.routine.repository;

import com.example.wecare.member.domain.Member;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineHistoryRepository extends JpaRepository<RoutineHistory, Long> {
    Optional<RoutineHistory> findByRoutineAndCompletedDate(Routine routine, LocalDate completedDate);

    List<RoutineHistory> findByRoutine_Dependent(Member routineDependent);

    List<RoutineHistory> findByRoutine(Routine routine);
}