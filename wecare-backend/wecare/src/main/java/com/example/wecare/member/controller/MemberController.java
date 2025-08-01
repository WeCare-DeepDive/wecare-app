package com.example.wecare.member.controller;

import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.dto.PartnerResponse;
import com.example.wecare.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "사용자 서비스", description = "사용자 정보 조회")
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "내 정보 조회",
            description = "사용자 정보 조회",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMe() {
        return ResponseEntity.ok(memberService.getMe());
    }

    @Operation(
            summary = "연결된 상대방 정보 조회",
            description = "민감 정보는 제외되며, 연결되지 않은 상대방은 조회 불가",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/partner")
    public ResponseEntity<PartnerResponse> getPartner(
            @RequestParam Long partnerId
    ) {
        return ResponseEntity.ok(memberService.getPartner(partnerId));
    }
}
