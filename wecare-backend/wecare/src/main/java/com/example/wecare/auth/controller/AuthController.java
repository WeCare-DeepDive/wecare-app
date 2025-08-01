package com.example.wecare.auth.controller;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.LoginResponse;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.auth.jwt.JwtUtil;
import com.example.wecare.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 서비스", description = "사용자 인증, 가입 등")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "회원가입",
            description = ""
    )
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request) + " 회원가입이 완료되었습니다.");
    }

    @Operation(
            summary = "로그인",
            description = ""
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(
            summary = "로그아웃",
            description = "Refresh-Token 헤더에 토큰 첨부 필요",
            security = {
                    @SecurityRequirement(name = "Authorization"),
                    @SecurityRequirement(name = "Refresh-Token")}
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String accessBearerToken,
            @Parameter(hidden = true)
            @RequestHeader("Refresh-Token") String refreshBearerToken
    ) {
        String accessToken = accessBearerToken.substring(7);
        String refreshToken = refreshBearerToken.substring(7);

        Long memberId = jwtUtil.getIdFromToken(refreshToken);

        authService.logout(memberId, accessToken, refreshToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @Operation(
            summary = "엑세스 토큰 재발급",
            description = "Refresh-Token 헤더에 토큰 첨부 필요",
            security = {
                    @SecurityRequirement(name = "Authorization"),
                    @SecurityRequirement(name = "Refresh-Token")}
    )
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String accessBearerToken,
            @Parameter(hidden = true)
            @RequestHeader("Refresh-Token") String refreshBearerToken
    ) {
        String accessToken = accessBearerToken.substring(7);
        String refreshToken = refreshBearerToken.substring(7);

        return ResponseEntity.ok(authService.reissue(accessToken, refreshToken));
    }
}

