package com.example.wecare.member.dto;

import com.example.wecare.member.code.Gender;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MemberResponse {
    private Long id;
    private String username;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}