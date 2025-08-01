package com.example.wecare.connection.dto;

import com.example.wecare.connection.code.RelationshipType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRelationRequest {
    @NotNull(message = "상대방과의 관계는 필수 입력 값입니다.")
    private RelationshipType relationshipType;
}
