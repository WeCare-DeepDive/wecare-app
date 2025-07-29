package com.example.wecare.member.service;

import com.example.wecare.invitation.domain.Invitation;
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
        List<Member> dependents = currentMember.getDependentConnections().stream()
                .map(Invitation::getDependent)
                .collect(Collectors.toList());
        return dependents.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MemberResponse convertToResponse(Member member) {
        Long guardianId = null;
        if (member.getRole() == Role.DEPENDENT && !member.getGuardianConnections().isEmpty()) {
            // 피보호자가 여러 보호자를 가질 수 있으므로, 여기서는 첫 번째 보호자의 ID를 가져옵니다.
            // 실제 비즈니스 로직에 따라 어떤 보호자의 ID를 보여줄지 결정해야 합니다.
            guardianId = member.getGuardianConnections().iterator().next().getGuardian().getId();
        }

        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .guardianId(guardianId)
                .build();
    }
}
