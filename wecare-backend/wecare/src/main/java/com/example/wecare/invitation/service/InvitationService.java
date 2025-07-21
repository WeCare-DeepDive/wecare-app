package com.example.wecare.invitation.service;

import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.redis.service.RedisService;
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
    private static final String INVITATION_CODE_PREFIX = "INVITE:";
    private static final long INVITATION_CODE_EXPIRATION_MINUTES = 10;

    private Long getCurrentMemberId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional
    public String generateInvitationCode() {
        Long guardianId = getCurrentMemberId();
        Member guardian = memberRepository.findById(guardianId)
                .orElseThrow(() -> new IllegalArgumentException("보호자를 찾을 수 없습니다."));

        if (guardian.getRole() != Role.GUARDIAN) {
            throw new IllegalArgumentException("초대 코드는 보호자만 생성할 수 있습니다.");
        }

        String invitationCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String redisKey = INVITATION_CODE_PREFIX + invitationCode;

        redisService.setValues(redisKey, guardianId.toString(), INVITATION_CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        return invitationCode;
    }

    @Transactional
    public void acceptInvitationCode(String code) {
        Long dependentId = getCurrentMemberId();
        Member dependent = memberRepository.findById(dependentId)
                .orElseThrow(() -> new IllegalArgumentException("피보호자를 찾을 수 없습니다."));

        if (dependent.getRole() != Role.DEPENDENT) {
            throw new IllegalArgumentException("초대 코드는 피보호자만 수락할 수 있습니다.");
        }

        if (dependent.getGuardian() != null) {
            throw new IllegalArgumentException("이미 보호자와 연결되어 있습니다.");
        }

        String redisKey = INVITATION_CODE_PREFIX + code;
        String guardianIdStr = redisService.getValues(redisKey);

        if (guardianIdStr == null) {
            throw new IllegalArgumentException("유효하지 않은 초대 코드입니다.");
        }

        Long guardianId = Long.parseLong(guardianIdStr);
        Member guardian = memberRepository.findById(guardianId)
                .orElseThrow(() -> new IllegalArgumentException("초대 코드를 생성한 보호자를 찾을 수 없습니다."));

        dependent.setGuardian(guardian);
        memberRepository.save(dependent);

        redisService.deleteValues(redisKey);
    }
}
