package com.example.wecare.invitation.service;

import com.example.wecare.common.code.GeneralResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.connection.code.RelationshipType;
import com.example.wecare.connection.domain.Connection;
import com.example.wecare.connection.repository.ConnectionRepository;
import com.example.wecare.invitation.dto.InvitationDto;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationService {
    private final MemberRepository memberRepository;
    private final ConnectionRepository connectionRepository;
    private final InvitationRedisService invitationRedisService;

    @Transactional(readOnly = true)
    public InvitationDto getInvitationCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = (Member) authentication.getPrincipal();

        String invitationCode = UUID.randomUUID().toString().substring(0, 8);
        invitationRedisService.saveInvitationCode(invitationCode, currentMember.getId());

        return InvitationDto.builder()
                .invitationCode(invitationCode)
                .build();
    }

    @Transactional
    public void acceptInvitationCode(String invitationCode, RelationshipType relationshipType) {
        // 초대 코드 검증
        Member inviter = validateInvitationCodeAndGetInviter(invitationCode);

        // 현재 사용자 정보 로드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member invitee = (Member) authentication.getPrincipal();

        // 역할 관계 정의
        Member guardian = inviter.getRole() == Role.GUARDIAN ? inviter : invitee;
        Member dependent = inviter.getRole() == Role.DEPENDENT ? invitee : inviter;

        Connection connection = connectionRepository.findByGuardianAndDependent(guardian, dependent)
                .orElse(
                        Connection.builder()
                                .guardian(guardian)
                                .dependent(dependent)
                                .relationshipType(relationshipType)
                                .build()
                );

        connection.setActive(true);

        // 연결 정보 저장
        connectionRepository.save(connection);

        // 초대 코드 만료
        invitationRedisService.deleteInvitationCode(invitationCode);
    }

    private Member validateInvitationCodeAndGetInviter(String invitationCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member invitee = (Member) auth.getPrincipal();

        // 초대 코드 존재 여부
        if (!invitationRedisService.validateInvitationCode(invitationCode)) {
            throw new ApiException(GeneralResponseCode.INVITATION_CODE_NOT_FOUND);
        }

        Long inviterId = invitationRedisService.getInviterId(invitationCode);

        // 초대자 존재 여부 검증
        Member inviter = memberRepository.findById(inviterId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.INVITER_NOT_FOUND));

        // 자가 초대 여부 검증
        if (invitee.getId().equals(inviterId)) {
            throw new ApiException(GeneralResponseCode.RECURSIVE_INVITATION_CODE);
        }

        // 초대자와 초대 받는 자 역할 검증
        if (invitee.getRole() == inviter.getRole()) {
            throw new ApiException(GeneralResponseCode.RECURSIVE_INVITATION_ROLE);
        }

        Member guardian = inviter.getRole() == Role.GUARDIAN ? inviter : invitee;
        Member dependent = inviter.getRole() == Role.DEPENDENT ? invitee : inviter;

        // 중복된 연결 검증
        if (connectionRepository.existsByGuardianIdAndDependentIdAndActiveTrue(guardian.getId(), dependent.getId())) {
            throw new ApiException(GeneralResponseCode.DUPLICATED_RELATIONSHIP);
        }

        // 초대자 정보 반환
        return inviter;
    }
}
