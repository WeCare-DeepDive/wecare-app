package com.example.wecare.connection.controller;

import com.example.wecare.connection.domain.Connection;
import com.example.wecare.connection.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping("")
    public ResponseEntity<List<Connection>> getMyConnections() {
        return ResponseEntity.ok(connectionService.getMyConnections());
    }

    @PatchMapping("/deactivate/{targetUserId}")
    public ResponseEntity<String> deactivateConnection(@PathVariable Long targetUserId) {
        connectionService.deactivateConnection(targetUserId);

        return ResponseEntity.ok("연결이 해제되었습니다.");
    }

    @PatchMapping("/reactivate/{targetUserId}")
    public ResponseEntity<String> reactivateConnection(@PathVariable Long targetUserId) {
        connectionService.reactivateConnection(targetUserId);

        return ResponseEntity.ok("연결이 재개 되었습니다.");
    }

    // 연결 정보 수정 ex) 보호자, 피보호자 간 관계 (가족, 친구 등)
    @PatchMapping("/relationship/{targetUserId}")
    public ResponseEntity<String> relationshipConnection(
            @PathVariable Long targetUserId
    ) {
        connectionService.updateRelationship(targetUserId);

        return ResponseEntity.ok("연결 정보가 수정되었습니다.");
    }
}
