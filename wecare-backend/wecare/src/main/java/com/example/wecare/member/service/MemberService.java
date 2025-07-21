package com.example.wecare.member.service;

import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private Long getCurrentMemberId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private Member getCurrentMember() {
        Long currentMemberId = getCurrentMemberId();
        return memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 사용자를 찾을 수 없습니다."));
    }

    public MemberResponse getMe() {
        Member currentMember = getCurrentMember();
        return convertToResponse(currentMember);
    }

    public List<MemberResponse> getMyDependents() {
        Member currentMember = getCurrentMember();
        if (currentMember.getRole() != Role.GUARDIAN) {
            throw new AccessDeniedException("보호자만 피보호자 목록을 조회할 수 있습니다.");
        }
        List<Member> dependents = memberRepository.findByGuardian(currentMember);
        return dependents.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MemberResponse convertToResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .name(member.getName())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .guardianId(member.getGuardian() != null ? member.getGuardian().getId() : null)
                .build();
    }
}
