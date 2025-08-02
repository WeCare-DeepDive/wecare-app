package com.example.wecare.unit;

import com.example.wecare.auth.jwt.JwtProperties;
import com.example.wecare.auth.jwt.JwtUtil;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtUtil jwtUtil;

    Member testMember = Member.builder()
            .id(3L)
            .name("test")
            .role(Role.GUARDIAN)
            .username("testtest1234")
            .build();

    Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken(
                testMember,
                "testPass",
                Collections.singleton(new SimpleGrantedAuthority(testMember.getRole().name()))
        );
    }

    @Test
    @DisplayName("엑세스 토큰과 리프레시 토큰이 정상적으로 생성되어야 한다.")
    void createToken_Success() {
        //given
        given(jwtProperties.getAccessExp())
                .willReturn(36000L);
        given(jwtProperties.getRefreshExp())
                .willReturn(36000L);
        given(jwtProperties.getSecretKey())
                .willReturn(Keys.hmacShaKeyFor("qwerqwerqwerqwerqwerqwerqwerqwer".getBytes(StandardCharsets.UTF_8)));

        // when
        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(authentication);

        // then
        assertNotNull(accessToken);
        assertNotNull(refreshToken);

        assertEquals(testMember.getId(), jwtUtil.getIdFromToken(accessToken));
        assertEquals(testMember.getId(), jwtUtil.getIdFromToken(refreshToken));

        Long memberId = jwtUtil.getIdFromToken(refreshToken);
        assertThat(memberId).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("유효한 토큰은 검증을 통과해야 한다.")
    void validateToken_Success() {
        // given
        given(jwtProperties.getAccessExp())
                .willReturn(36000L);
        given(jwtProperties.getSecretKey())
                .willReturn(Keys.hmacShaKeyFor("qwerqwerqwerqwerqwerqwerqwerqwer".getBytes(StandardCharsets.UTF_8)));
        String token = jwtUtil.generateAccessToken(authentication);

        // when
        boolean isValid = jwtUtil.validateToken(token);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("만료된 토큰은 검증에 실패해야 한다.")
    void validateToken_Fail_Expired() throws InterruptedException {
        // given
        given(jwtProperties.getAccessExp())
                .willReturn(10L);
        given(jwtProperties.getSecretKey())
                .willReturn(Keys.hmacShaKeyFor("qwerqwerqwerqwerqwerqwerqwerqwer".getBytes(StandardCharsets.UTF_8)));
        String expiredToken = jwtUtil.generateAccessToken(authentication);

        // when
        Thread.sleep(100); // Ensure the token is expired
        boolean isValid = jwtUtil.validateToken(expiredToken);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("손상된 토큰은 검증에 실패해야 한다.")
    void validateToken_Fail_Invalid() {
        // given
        String invalidToken = "invalid.token.string";

        // when
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("토큰의 만료 시간을 정상적으로 조회해야 한다.")
    void getExpiration_Success() {
        // given
        given(jwtProperties.getAccessExp())
                .willReturn(36000L);
        given(jwtProperties.getSecretKey())
                .willReturn(Keys.hmacShaKeyFor("qwerqwerqwerqwerqwerqwerqwerqwer".getBytes(StandardCharsets.UTF_8)));

        String token = jwtUtil.generateAccessToken(authentication);

        // when
        Date expiration = jwtUtil.getExpirationFromToken(token);

        // then
        assertTrue(expiration != null && expiration.getTime() > 0L);
    }
}
