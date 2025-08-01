package com.example.wecare.member.dto;

import com.example.wecare.member.code.Gender;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartnerResponse {
    private Long id;
    private String username;
    private String name;
    private Gender gender;
    private Role role;

    public static PartnerResponse fromEntity(Member member) {
        return PartnerResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .gender(member.getGender())
                .role(member.getRole())
                .build();
    }
}
