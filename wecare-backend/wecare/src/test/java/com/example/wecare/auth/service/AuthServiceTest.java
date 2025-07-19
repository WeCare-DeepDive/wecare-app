package com.example.wecare.auth.service;

import com.example.wecare.auth.dto.LoginRequest;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.auth.dto.TokenDto;
import com.example.wecare.auth.jwt.JwtTokenProvider;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.redis.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.wecare.member.domain.Gender;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.example.wecare.member.domain.Role;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisService redisService;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // Given
        SignUpRequest request = new SignUpRequest();
        request.setMemberId("testuser");
        request.setPassword("password123");
        request.setName("테스트");
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.parse("1990-01-01"));
        request.setRole(Role.GUARDIAN);

        when(memberRepository.findByMemberId(request.getMemberId())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        authService.signUp(request);

        // Then
        verify(memberRepository, times(1)).findByMemberId(request.getMemberId());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 등록된 아이디")
    void signUp_fail_alreadyExists() {
        // Given
        SignUpRequest request = new SignUpRequest();
        request.setMemberId("existinguser");
        request.setPassword("password123");
        request.setName("테스트");
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.parse("1990-01-01"));
        request.setRole(Role.GUARDIAN);

        when(memberRepository.findByMemberId(request.getMemberId())).thenReturn(Optional.of(new Member()));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.signUp(request));
        assertEquals("이미 등록된 아이디입니다.", exception.getMessage());

        verify(memberRepository, times(1)).findByMemberId(request.getMemberId());
        verify(passwordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인 성공 - 토큰 발급")
    void login_success() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMemberId("testuser");
        loginRequest.setPassword("password123");

        TokenDto expectedTokenDto = TokenDto.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.createAccessToken(authentication)).thenReturn("mockAccessToken");
        when(jwtTokenProvider.createRefreshToken(authentication)).thenReturn("mockRefreshToken");
        when(jwtTokenProvider.getRefreshTokenExpirationTime()).thenReturn(3600000L); // 1 hour
        when(authentication.getName()).thenReturn("testuser");

        // When
        TokenDto result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedTokenDto.getAccessToken(), result.getAccessToken());
        assertEquals(expectedTokenDto.getRefreshToken(), result.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).createAccessToken(authentication);
        verify(jwtTokenProvider, times(1)).createRefreshToken(authentication);
        verify(redisService, times(1)).setValues(eq("testuser"), eq("mockRefreshToken"), eq(3600000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 자격 증명")
    void login_fail_badCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMemberId("wronguser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When & Then
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, never()).createAccessToken(any(Authentication.class));
        verify(redisService, never()).setValues(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // Given
        String accessToken = "validAccessToken";
        Long expirationTime = 1000L; // 1 second

        when(jwtTokenProvider.validateToken(accessToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(accessToken)).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(redisService.getValues("testuser")).thenReturn("someRefreshToken");
        when(jwtTokenProvider.getExpiration(accessToken)).thenReturn(expirationTime);

        // When
        authService.logout(accessToken);

        // Then
        verify(jwtTokenProvider, times(1)).validateToken(accessToken);
        verify(jwtTokenProvider, times(1)).getAuthentication(accessToken);
        verify(redisService, times(1)).getValues("testuser");
        verify(redisService, times(1)).deleteValues("testuser");
        verify(jwtTokenProvider, times(1)).getExpiration(accessToken);
        verify(redisService, times(1)).setValues(eq(accessToken), eq("logout"), eq(expirationTime), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("로그아웃 실패 - 유효하지 않은 Access Token")
    void logout_fail_invalidAccessToken() {
        // Given
        String accessToken = "invalidAccessToken";

        when(jwtTokenProvider.validateToken(accessToken)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.logout(accessToken));
        assertEquals("유효하지 않은 Access Token 입니다.", exception.getMessage());

        verify(jwtTokenProvider, times(1)).validateToken(accessToken);
        verify(jwtTokenProvider, never()).getAuthentication(anyString());
        verify(redisService, never()).getValues(anyString());
        verify(redisService, never()).deleteValues(anyString());
        verify(jwtTokenProvider, never()).getExpiration(anyString());
        verify(redisService, never()).setValues(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        // Given
        String refreshToken = "validRefreshToken";
        TokenDto expectedTokenDto = TokenDto.builder()
                .accessToken("newAccessToken")
                .refreshToken("newRefreshToken")
                .build();

        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(refreshToken)).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(redisService.getValues("testuser")).thenReturn(refreshToken);
        when(jwtTokenProvider.createAccessToken(authentication)).thenReturn("newAccessToken");
        when(jwtTokenProvider.createRefreshToken(authentication)).thenReturn("newRefreshToken");
        when(jwtTokenProvider.getRefreshTokenExpirationTime()).thenReturn(3600000L);

        // When
        TokenDto result = authService.reissue(refreshToken);

        // Then
        assertNotNull(result);
        assertEquals(expectedTokenDto.getAccessToken(), result.getAccessToken());
        assertEquals(expectedTokenDto.getRefreshToken(), result.getRefreshToken());

        verify(jwtTokenProvider, times(1)).validateToken(refreshToken);
        verify(jwtTokenProvider, times(1)).getAuthentication(refreshToken);
        verify(redisService, times(1)).getValues("testuser");
        verify(jwtTokenProvider, times(1)).createAccessToken(authentication);
        verify(jwtTokenProvider, times(1)).createRefreshToken(authentication);
        verify(redisService, times(1)).setValues(eq("testuser"), eq("newRefreshToken"), eq(3600000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효하지 않은 Refresh Token")
    void reissue_fail_invalidRefreshToken() {
        // Given
        String refreshToken = "invalidRefreshToken";

        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.reissue(refreshToken));
        assertEquals("유효하지 않은 Refresh Token 입니다.", exception.getMessage());

        verify(jwtTokenProvider, times(1)).validateToken(refreshToken);
        verify(jwtTokenProvider, never()).getAuthentication(anyString());
        verify(redisService, never()).getValues(anyString());
        verify(jwtTokenProvider, never()).createAccessToken(any(Authentication.class));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - Redis Refresh Token 불일치")
    void reissue_fail_redisMismatch() {
        // Given
        String refreshToken = "validRefreshToken";
        String redisRefreshToken = "differentRefreshToken";

        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(refreshToken)).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(redisService.getValues("testuser")).thenReturn(redisRefreshToken);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.reissue(refreshToken));
        assertEquals("Refresh Token 정보가 일치하지 않습니다.", exception.getMessage());

        verify(jwtTokenProvider, times(1)).validateToken(refreshToken);
        verify(jwtTokenProvider, times(1)).getAuthentication(refreshToken);
        verify(redisService, times(1)).getValues("testuser");
        verify(jwtTokenProvider, never()).createAccessToken(any(Authentication.class));
    }
}