package com.example.wecare.unit;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.LoginResponse;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.auth.jwt.JwtProperties;
import com.example.wecare.auth.jwt.JwtRedisService;
import com.example.wecare.auth.jwt.JwtUtil;
import com.example.wecare.auth.service.AuthService;
import com.example.wecare.common.code.AuthResponseCode;
import com.example.wecare.common.code.GeneralResponseCode;
import com.example.wecare.member.code.Gender;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private JwtRedisService jwtRedisService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    Member testMember = Member.builder()
            .id(3L)
            .role(Role.GUARDIAN)
            .name("test")
            .username("test1234")
            .birthDate(Timestamp.valueOf(LocalDateTime.of(2000, 2, 20, 0, 00)))
            .build();

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .username(testMember.getUsername())
                .password("password123")
                .name("테스트")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("1990-01-01"))
                .role(Role.GUARDIAN)
                .build();

        when(memberRepository.existsMemberByUsername(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // When
        authService.signUp(request);

        // Then
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 등록된 아이디")
    void signUp_fail_alreadyExists() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .username("existinguser")
                .password("password123")
                .name("테스트")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("1990-01-01"))
                .role(Role.GUARDIAN)
                .build();

        when(memberRepository.existsMemberByUsername(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> authService.signUp(request));
        assertEquals(GeneralResponseCode.DUPLICATED_USERNAME.getMessage(), exception.getMessage());

        verify(memberRepository, times(1)).existsMemberByUsername(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인 성공 - 토큰 발급")
    void login_success() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(testMember.getUsername());
        loginRequest.setPassword("password123");

        LoginResponse expectedLoginResponse = LoginResponse.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        Authentication mockAuth = Mockito.mock(Authentication.class);

        when(jwtUtil.generateAccessToken(any())).thenReturn("mockAccessToken");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("mockRefreshToken");
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(mockAuth.getPrincipal()).thenReturn(testMember);

        // When
        LoginResponse result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedLoginResponse.getAccessToken(), result.getAccessToken());
        assertEquals(expectedLoginResponse.getRefreshToken(), result.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateAccessToken(any());
        verify(jwtUtil, times(1)).generateRefreshToken(any());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // Given
        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";
        Date expirationTime = new Date(System.currentTimeMillis() + 36000L); // 1 second

        SecurityContext mockContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuth = Mockito.mock(Authentication.class);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(jwtUtil.validateToken(anyString())).thenReturn(true);

        // When
        authService.logout(testMember.getId(), accessToken, refreshToken);

        // Then
        verify(jwtUtil, times(1)).validateToken(anyString());
        verify(jwtRedisService, times(2)).logoutToken(anyString());
    }

    @Test
    @DisplayName("로그아웃 실패 - 유효하지 않은 Access Token")
    void logout_fail_invalidAccessToken() {
        // Given
        String accessToken = "invalidAccessToken";
        String refreshToken = "validRefreshToken";

        given(jwtUtil.validateToken(anyString())).willReturn(false);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> authService.logout(testMember.getId(), accessToken, refreshToken));
        assertEquals(AuthResponseCode.INVALID_TOKEN.getMessage(), exception.getMessage());

        verify(jwtUtil, times(1)).validateToken(anyString());
        verify(jwtUtil, never()).getExpirationFromToken(anyString());
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        // Given
        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";
        LoginResponse expectedLoginResponse = LoginResponse.builder()
                .accessToken("newAccessToken")
                .refreshToken("newRefreshToken")
                .build();

        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtil.generateAccessToken(any())).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("newRefreshToken");
        when(jwtRedisService.hasRefreshToken(anyString())).thenReturn(true);

        // When
        LoginResponse result = authService.reissue(accessToken, refreshToken);

        // Then
        assertNotNull(result);
        assertEquals(expectedLoginResponse.getAccessToken(), result.getAccessToken());
        assertEquals(expectedLoginResponse.getRefreshToken(), result.getRefreshToken());

        verify(jwtUtil, times(1)).validateToken(anyString());
        verify(jwtUtil, times(1)).generateRefreshToken(any());
        verify(jwtUtil, times(1)).generateAccessToken(any());
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효하지 않은 Refresh Token")
    void reissue_fail_invalidRefreshToken() {
        // Given
        String accessToken = "validToken";
        String refreshToken = "invalidRefreshToken";

        when(jwtUtil.validateToken(refreshToken)).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> authService.reissue(accessToken, refreshToken));
        assertEquals(AuthResponseCode.INVALID_TOKEN.getMessage(), exception.getMessage());

        verify(jwtUtil, times(1)).validateToken(refreshToken);
        verify(jwtUtil, never()).generateAccessToken(any());
    }

    @Test
    @DisplayName("토큰 재발급 실패 - Redis Refresh Token 불일치")
    void reissue_fail_redisMismatch() {
        // Given
        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";
        String redisRefreshToken = "differentRefreshToken";

        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        // When & Then
        Exception exception = assertThrows(Exception.class, () -> authService.reissue(accessToken, refreshToken));
        assertEquals(AuthResponseCode.INVALID_TOKEN.getMessage(), exception.getMessage());

        verify(jwtUtil, times(1)).validateToken(refreshToken);
        verify(jwtUtil, never()).generateAccessToken(any(Authentication.class));
    }
}