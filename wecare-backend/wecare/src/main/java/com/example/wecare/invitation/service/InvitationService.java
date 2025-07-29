package com.example.wecare.invitation.service;

import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.invitation.repository.InvitationRepository;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final InvitationRepository invitationRepository;
    private static final String INVITATION_CODE_PREFIX = "INVITE:";
    private static final long INVITATION_CODE_EXPIRATION_MINUTES = 10;

    private Long getCurrentMemberId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional
    public String generateInvitationCode() {
        Long currentUserId = getCurrentMemberId();
        memberRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String invitationCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String redisKey = INVITATION_CODE_PREFIX + invitationCode;

        redisService.setValues(redisKey, currentUserId.toString(), INVITATION_CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        return invitationCode;
    }

    @Transactional
    public void acceptInvitationCode(String code) {
        Long currentUserId = getCurrentMemberId();
        Member currentUser = memberRepository.findByIdWithPessimisticLock(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("현재 사용자를 찾을 수 없습니다."));

        String redisKey = INVITATION_CODE_PREFIX + code;
        String inviterIdStr = redisService.getValues(redisKey);

        if (inviterIdStr == null) {
            throw new IllegalArgumentException("유효하지 않은 초대 코드입니다.");
        }

        Long inviterId = Long.parseLong(inviterIdStr);
        if (currentUserId.equals(inviterId)) {
            throw new IllegalArgumentException("자신의 초대 코드는 수락할 수 없습니다.");
        }

        Member inviter = memberRepository.findByIdWithPessimisticLock(inviterId)
                .orElseThrow(() -> new IllegalArgumentException("초대 코드를 생성한 사용자를 찾을 수 없습니다."));

        if (currentUser.getRole() == inviter.getRole()) {
            throw new IllegalArgumentException("동일한 역할의 사용자와는 연결할 수 없습니다.");
        }

        Member guardian;
        Member dependent;

        if (currentUser.getRole() == Role.GUARDIAN) {
            guardian = currentUser;
            dependent = inviter;
        } else {
            guardian = inviter;
            dependent = currentUser;
        }

        if (invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(guardian.getId(), dependent.getId())) {
            throw new IllegalArgumentException("이미 연결된 관계입니다.");
        }

        Invitation newInvitation = new Invitation();
        newInvitation.setGuardian(guardian);
        newInvitation.setDependent(dependent);
        newInvitation.setActive(true); // 명시적으로 활성화 상태로 저장

        invitationRepository.save(newInvitation);

        redisService.deleteValues(redisKey);
    }

    @Transactional
    public void deleteConnection(Long targetUserId) {
        Long currentUserId = getCurrentMemberId();
        Member currentUser = memberRepository.findByIdWithPessimisticLock(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("현재 사용자를 찾을 수 없습니다."));

        Member targetUser = memberRepository.findByIdWithPessimisticLock(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("대상 사용자를 찾을 수 없습니다."));

        // 연결 관계를 파악하여 guardian과 dependent를 설정
        Member guardianInConnection;
        Member dependentInConnection;

        if (currentUser.getRole() == Role.GUARDIAN && targetUser.getRole() == Role.DEPENDENT) {
            guardianInConnection = currentUser;
            dependentInConnection = targetUser;
        } else if (currentUser.getRole() == Role.DEPENDENT && targetUser.getRole() == Role.GUARDIAN) {
            guardianInConnection = targetUser;
            dependentInConnection = currentUser;
        } else {
            throw new IllegalArgumentException("보호자와 피보호자 간의 연결만 삭제할 수 있습니다.");
        }

        // Invitation 엔티티 찾기
        Invitation invitation = invitationRepository.findById(new com.example.wecare.invitation.domain.InvitationId(guardianInConnection.getId(), dependentInConnection.getId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 연결입니다."));

        // 활성화된 연결인지 확인
        if (!invitation.isActive()) {
            throw new IllegalArgumentException("이미 비활성화된 연결입니다.");
        }

        // 소프트 삭제
        invitation.setActive(false);
        invitationRepository.save(invitation);
    }
}
