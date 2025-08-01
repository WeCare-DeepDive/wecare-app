package com.example.wecare.common.security;

import com.example.wecare.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AccessHandler {
    public final boolean ownershipCheck(Long resourceId) {
        return isResourceOwner(resourceId);
    }

    public final Member getCurrentMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Member) auth.getPrincipal();
    }

    abstract boolean isResourceOwner(Long resourceId);
}
