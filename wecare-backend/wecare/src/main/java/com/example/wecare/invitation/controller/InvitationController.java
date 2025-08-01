package com.example.wecare.invitation.controller;

import com.example.wecare.invitation.dto.AcceptInvitationRequest;
import com.example.wecare.invitation.dto.InvitationDto;
import com.example.wecare.invitation.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @GetMapping("/generate")
    public ResponseEntity<InvitationDto> generateInvitationCode() {
        return ResponseEntity.ok(invitationService.getInvitationCode());
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptInvitationCode(
            @Valid @RequestBody AcceptInvitationRequest request
    ) {
        invitationService.acceptInvitationCode(request.getInvitationCode(), request.getRelationshipType());
        return ResponseEntity.ok("초대가 수락되었습니다.");
    }
}
