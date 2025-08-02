package com.example.wecare.connection.domain;

import com.example.wecare.connection.code.RelationshipType;
import com.example.wecare.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guardian_id")
    private Member guardian;

    @ManyToOne
    @JoinColumn(name = "dependent_id")
    private Member dependent;

    @Column(name = "is_active", insertable = false)
    private boolean active;

    @Column(name = "relationship_type") // 관계 필드 추가
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private RelationshipType relationshipType;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}
