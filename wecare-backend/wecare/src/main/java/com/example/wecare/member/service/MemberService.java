package com.example.wecare.member.service;

import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 정보 조회, 수정 등 member 도메인 관련 로직 추가 예정
}