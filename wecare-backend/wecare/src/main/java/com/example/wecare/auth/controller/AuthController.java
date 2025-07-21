package com.example.wecare.auth.controller;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.LoginResponse;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.auth.dto.TokenDto;
import com.example.wecare.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenDto tokenDto = authService.login(loginRequest);
        return ResponseEntity.ok(LoginResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken) {
        String token = extractToken(bearerToken);
        authService.logout(token);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@RequestHeader("Authorization") String bearerToken) {
        String token = extractToken(bearerToken);
        TokenDto tokenDto = authService.reissue(token);
        return ResponseEntity.ok(LoginResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build());
    }

    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Authorization 헤더 형식이 잘못되었습니다. 'Bearer <token>' 형식을 사용해주세요.");
    }
}

