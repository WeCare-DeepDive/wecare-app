package com.example.wecare.member.controller;

import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.dto.MemberRelationshipDto;
import com.example.wecare.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // MyPage 접근 시
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMe() {
        MemberResponse memberResponse = memberService.getMe();
        return ResponseEntity.ok(memberResponse);
    }

    // 현재 로그인한 사용자와 연결된 모든 관계 정보를 조회
    @GetMapping("/me/relationships")
    public ResponseEntity<List<MemberRelationshipDto>> getMyRelationships() {
        List<MemberRelationshipDto> relationships = memberService.getMemberRelationships();
        return ResponseEntity.ok(relationships);
    }
}
