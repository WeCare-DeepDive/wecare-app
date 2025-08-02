package com.example.wecare.common.security;

import com.example.wecare.common.code.GeneralResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.connection.repository.ConnectionRepository;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.routine.domain.Routine;
import com.example.wecare.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineAccessHandler extends AccessHandler {
    private final RoutineRepository routineRepository;
    private final ConnectionRepository connectionRepository;

    @Override
    boolean isResourceOwner(Long routineId) {
        Member currentMember = getCurrentMember();

        if (currentMember.getRole() != Role.DEPENDENT) {
            Routine routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new ApiException(GeneralResponseCode.ROUTINE_NOT_FOUND));

            return connectionRepository.existsByGuardianAndDependentAndActiveTrue(
                    currentMember, routine.getDependent()
            );
        }

        return routineRepository.findById(routineId)
                .filter((r) -> currentMember.getId().equals(r.getDependent().getId()))
                .isPresent();
    }
}
