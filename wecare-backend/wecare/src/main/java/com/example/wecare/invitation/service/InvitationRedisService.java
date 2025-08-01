package com.example.wecare.invitation.service;

import com.example.wecare.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationRedisService {
    private final RedisService redisService;
    private static final String INVITATION_CODE_PREFIX = "INVITE:";
    private static final long INVITATION_CODE_EXPIRATION = 10 * 60 * 60 * 1000; // 10 minutes

    public void saveInvitationCode(String invitationCode, Long inviterId) {
        redisService.setValues(
                INVITATION_CODE_PREFIX + invitationCode,
                inviterId.toString(),
                INVITATION_CODE_EXPIRATION
        );
    }

    public void deleteInvitationCode(String invitationCode) {
        redisService.deleteValues(invitationCode);
    }

    public Long getInviterId(String invitationCode) {
        String inviterIdString = redisService.getValues(INVITATION_CODE_PREFIX + invitationCode);

        return Long.parseLong(inviterIdString);
    }

    public boolean validateInvitationCode(String invitationCode) {
        return redisService.hasKey(INVITATION_CODE_PREFIX + invitationCode);
    }
}
