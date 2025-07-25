package com.example.wecare.routine.repository;

import com.example.wecare.routine.domain.RoutineAlarmSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineAlarmSettingRepository extends JpaRepository<RoutineAlarmSetting, Long> {
}