package com.example.wecare.connection.controller;

import com.example.wecare.connection.dto.ConnectionDetailDto;
import com.example.wecare.connection.dto.ConnectionDto;
import com.example.wecare.connection.dto.UpdateRelationshipRequest;
import com.example.wecare.connection.service.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
@Tag(name = "사용자 연결 서비스", description = "보호자, 비보호자 간 연결")
public class ConnectionController {
    private final ConnectionService connectionService;

    @Operation(
            summary = "내 연결 정보 리스트 조회",
            description = "",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("")
    public ResponseEntity<List<ConnectionDto>> getMyConnections() {
        return ResponseEntity.ok(connectionService.getMyConnections());
    }

    @Operation(
            summary = "내 연결 세부 정보 리스트 조회",
            description = "상대방의 이름 필드 포함",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/details")
    public ResponseEntity<List<ConnectionDetailDto>> getMyDetailConnections() {
        return ResponseEntity.ok(connectionService.getMyDetailConnections());
    }

    @Operation(
            summary = "연결 해제하기",
            description = "Soft-delete, 다시 연결 가능하며 데이터 보존됨",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{connectionId}/deactivate")
    public ResponseEntity<String> deactivateConnection(@PathVariable Long connectionId) {
        connectionService.deactivateConnection(connectionId);

        return ResponseEntity.ok("연결이 해제되었습니다.");
    }

    @Operation(
            summary = "연결 정보 수정",
            description = "연결 간 관계 재설정",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{connectionId}")
    public ResponseEntity<String> relationshipConnection(
            @PathVariable Long connectionId,
            @RequestBody @Valid UpdateRelationshipRequest relationshipType
    ) {
        connectionService.updateRelationship(connectionId, relationshipType);

        return ResponseEntity.ok("연결 정보가 수정되었습니다.");
    }
}
