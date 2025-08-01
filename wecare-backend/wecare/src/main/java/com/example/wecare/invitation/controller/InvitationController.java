package com.example.wecare.invitation.controller;

import com.example.wecare.invitation.dto.AcceptInvitationRequest;
import com.example.wecare.invitation.dto.InvitationDto;
import com.example.wecare.invitation.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;
    
    @Operation(
            summary = "초대 코드 생성",
            description = "본인의 초대 코드 생성, 유효 시간 10분",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/generate")
    public ResponseEntity<InvitationDto> generateInvitationCode() {
        return ResponseEntity.ok(invitationService.getInvitationCode());
    }

    @Operation(
            summary = "초대 코드 수락",
            description = "상대방의 초대 코드를 수락하여 연결 수립",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PostMapping("/accept")
    public ResponseEntity<String> acceptInvitationCode(
            @Valid @RequestBody AcceptInvitationRequest request
    ) {
        invitationService.acceptInvitationCode(request.getInvitationCode(), request.getRelationshipType());
        return ResponseEntity.ok("초대가 수락되었습니다.");
    }
}
