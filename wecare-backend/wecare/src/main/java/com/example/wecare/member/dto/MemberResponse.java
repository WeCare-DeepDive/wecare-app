package com.example.wecare.member.dto;

import com.example.wecare.member.domain.Gender;
import com.example.wecare.member.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MemberResponse {
    private Long id;
    private String memberId;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private Role role;
    private Long guardianId; // 피보호자인 경우 보호자의 ID
}
