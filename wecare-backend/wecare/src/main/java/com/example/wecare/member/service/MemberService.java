package com.example.wecare.member.service;

import com.example.wecare.common.code.AuthResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.dto.PartnerResponse;
import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    // 현재 로그인한 사용자 본인의 정보를 조회
    @Transactional(readOnly = true)
    public MemberResponse getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = (Member) authentication.getPrincipal();

        return MemberResponse.fromEntity(currentMember);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@partnerAccessHandler.ownershipCheck(#partnerId)")
    public PartnerResponse getPartner(Long partnerId) {
        Member partner = memberRepository.findById(partnerId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        return PartnerResponse.fromEntity(partner);
    }
}
