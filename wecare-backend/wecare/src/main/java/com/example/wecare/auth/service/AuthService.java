package com.example.wecare.auth.service;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.LoginResponse;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.auth.jwt.JwtRedisService;
import com.example.wecare.auth.jwt.JwtUtil;
import com.example.wecare.common.code.AuthResponseCode;
import com.example.wecare.common.code.GeneralResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtRedisService jwtRedisService;

    @Transactional
    public String signUp(SignUpRequest request) {
        if (memberRepository.existsMemberByUsername(request.getUsername())) {
            throw new ApiException(GeneralResponseCode.DUPLICATED_USERNAME);
        }

        if (request.getBirthDate().isAfter(LocalDate.now())){
            throw new ApiException(GeneralResponseCode.INVALID_REQUEST, "생년월일은 현재보다 미래일 수 없습니다.");
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .name(request.getName())
                .role(request.getRole())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        member = memberRepository.save(member);

        return member.getName();
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        Member member = (Member) auth.getPrincipal();

        String refreshToken = jwtUtil.generateRefreshToken(auth);
        jwtRedisService.saveRefreshToken(refreshToken);

        return LoginResponse.builder()
                .memberId(member.getId())
                .accessToken(jwtUtil.generateAccessToken(auth))
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(Long memberId, String accessToken, String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            log.error("Invalid JWT token inspected : {} in Logging out {}", refreshToken, LocalDateTime.now());
            throw new ApiException(AuthResponseCode.INVALID_TOKEN);
        }

        memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));

        jwtRedisService.deleteRefreshToken(refreshToken);
        jwtRedisService.logoutToken(accessToken); //Redis 블랙리스트에 토큰 추가
        jwtRedisService.logoutToken(refreshToken);

        SecurityContextHolder.clearContext(); //인증 정보 초기화
    }

    @Transactional(readOnly = true)
    public LoginResponse reissue(String accessToken, String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken) || !jwtRedisService.hasRefreshToken(refreshToken)) {
            throw new ApiException(AuthResponseCode.INVALID_TOKEN);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 4. 새로운 토큰 생성
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(auth))
                .refreshToken(jwtUtil.generateRefreshToken(auth))
                .build();

        jwtRedisService.saveRefreshToken(loginResponse.getRefreshToken());

        //기존 Token 만료
        jwtRedisService.deleteRefreshToken(refreshToken);
        jwtRedisService.logoutToken(refreshToken);
        jwtRedisService.logoutToken(accessToken);

        return loginResponse;
    }
}

