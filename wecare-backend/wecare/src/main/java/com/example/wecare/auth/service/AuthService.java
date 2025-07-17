package com.example.wecare.auth.service;

import com.example.wecare.member.domain.Member;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (memberRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }

        Member member = new Member();
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setBirthDate(request.getBirthDate().atStartOfDay());
        member.setPhone(request.getPhone());
        member.setRole(request.getRole());

        memberRepository.save(member);
    }
}
