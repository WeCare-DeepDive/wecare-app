package com.example.wecare.auth.controller;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.LoginResponse;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.auth.jwt.JwtUtil;
import com.example.wecare.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request) + " 회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String accessBearerToken,
            @RequestHeader("Refresh-Token") String refreshBearerToken
    ) {
        String accessToken = accessBearerToken.substring(7);
        String refreshToken = refreshBearerToken.substring(7);

        Long memberId = jwtUtil.getIdFromToken(refreshToken);

        authService.logout(memberId, accessToken, refreshToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(
            @RequestHeader("Authorization") String accessBearerToken,
            @RequestHeader("Refresh-Token") String refreshBearerToken
    ) {
        String accessToken = accessBearerToken.substring(7);
        String refreshToken = refreshBearerToken.substring(7);

        return ResponseEntity.ok(authService.reissue(accessToken, refreshToken));
    }
}

