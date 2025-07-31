package com.example.wecare.member.dto;

import com.example.wecare.invitation.domain.RelationshipType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberRelationshipDto {
    private Long memberId;
    private String relationshipType;
}
