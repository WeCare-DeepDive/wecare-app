package com.example.wecare.member.controller;

import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.dto.PartnerResponse;
import com.example.wecare.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMe() {
        return ResponseEntity.ok(memberService.getMe());
    }

    @GetMapping("/partner")
    public ResponseEntity<PartnerResponse> getPartner(
            @RequestParam Long partnerId
    ) {
        return ResponseEntity.ok(memberService.getPartner(partnerId));
    }
}
