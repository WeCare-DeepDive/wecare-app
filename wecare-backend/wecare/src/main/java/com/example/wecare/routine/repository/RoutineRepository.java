package com.example.wecare.routine.repository;

import com.example.wecare.routine.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByGuardianId(Long guardianId);
    List<Routine> findByDependentId(Long dependentId);
}