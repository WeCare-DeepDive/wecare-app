package com.example.wecare.routine.repository;

import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.domain.RoutineAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoutineAlertRepository extends JpaRepository<RoutineAlert, Long> {
    Optional<RoutineAlert> findByRoutine(Routine routine);
}