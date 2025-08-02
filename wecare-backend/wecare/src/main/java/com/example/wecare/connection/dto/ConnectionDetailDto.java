package com.example.wecare.connection.dto;


import com.example.wecare.connection.code.RelationshipType;
import com.example.wecare.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDetailDto {
    private Long id;
    private Long guardianId;
    private Long dependentId;
    private String partnerName;
    private boolean active;
    private RelationshipType relationshipType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ConnectionDetailDto fromConnectionDto(ConnectionDto connection, Member partner) {
        return ConnectionDetailDto.builder()
                .id(connection.getId())
                .partnerName(partner.getName())
                .guardianId(connection.getGuardianId())
                .dependentId(connection.getDependentId())
                .active(connection.isActive())
                .relationshipType(connection.getRelationshipType())
                .build();
    }
}
