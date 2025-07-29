package com.example.wecare.invitation.controller;

import com.example.wecare.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateInvitationCode() {
        String code = invitationService.generateInvitationCode();
        return ResponseEntity.ok(Map.of("invitationCode", code));
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvitationCode(@RequestBody Map<String, String> request) {
        String code = request.get("invitationCode");
        invitationService.acceptInvitationCode(code);
        return ResponseEntity.ok().build();
    }
}
