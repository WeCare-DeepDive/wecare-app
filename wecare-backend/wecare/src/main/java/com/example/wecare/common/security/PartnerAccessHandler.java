package com.example.wecare.common.security;

import com.example.wecare.connection.repository.ConnectionRepository;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartnerAccessHandler extends AccessHandler {
    private final ConnectionRepository connectionRepository;

    @Override
    boolean isResourceOwner(Long memberId) {
        Member currentMember = getCurrentMember();

        if (currentMember.getRole() == Role.GUARDIAN) {
            return connectionRepository.existsByGuardianIdAndDependentIdAndActiveTrue(currentMember.getId(), memberId);
        }

        return currentMember.getId().equals(memberId);
    }
}
