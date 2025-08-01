package com.example.wecare.connection.dto;


import com.example.wecare.connection.code.RelationshipType;
import com.example.wecare.connection.domain.Connection;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto {
    private Long id;
    private Long guardianId;
    private Long dependentId;
    private boolean active;
    private RelationshipType relationshipType;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static ConnectionDto fromEntity(Connection connection) {
        return ConnectionDto.builder()
                .id(connection.getId())
                .guardianId(connection.getGuardian().getId())
                .dependentId(connection.getDependent().getId())
                .active(connection.isActive())
                .relationshipType(connection.getRelationshipType())
                .build();
    }
}
