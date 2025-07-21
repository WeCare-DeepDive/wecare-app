
package com.example.wecare.auth.jwt;

import com.example.wecare.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken(
                "testUser",
                null,
                Collections.singleton(new SimpleGrantedAuthority(Role.GUARDIAN.name()))
        );
    }

    @Test
    @DisplayName("엑세스 토큰과 리프레시 토큰이 정상적으로 생성되어야 한다.")
    void createToken_Success() {
        // when
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // then
        assertThat(accessToken).isNotNull();
        assertThat(refreshToken).isNotNull();

        Authentication authFromAccessToken = jwtTokenProvider.getAuthentication(accessToken);
        assertThat(authFromAccessToken.getName()).isEqualTo("testUser");
        assertThat(authFromAccessToken.getAuthorities().stream().map(GrantedAuthority::getAuthority)).containsExactly(Role.GUARDIAN.name());

        Authentication authFromRefreshToken = jwtTokenProvider.getAuthentication(refreshToken);
        assertThat(authFromRefreshToken.getName()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("유효한 토큰은 검증을 통과해야 한다.")
    void validateToken_Success() {
        // given
        String token = jwtTokenProvider.createAccessToken(authentication);

        // when
        boolean isValid = jwtTokenProvider.validateToken(token);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("만료된 토큰은 검증에 실패해야 한다.")
    void validateToken_Fail_Expired() throws InterruptedException {
        // given
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpirationTime", 0L);
        String expiredToken = jwtTokenProvider.createAccessToken(authentication);

        // when
        Thread.sleep(10); // Ensure the token is expired
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // then
        assertFalse(isValid);

        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpirationTime", 86400000L); // Restore original value
    }

    @Test
    @DisplayName("손상된 토큰은 검증에 실패해야 한다.")
    void validateToken_Fail_Invalid() {
        // given
        String invalidToken = "invalid.token.string";

        // when
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("토큰에서 인증 정보를 정상적으로 추출해야 한다.")
    void getAuthentication_Success() {
        // given
        String token = jwtTokenProvider.createAccessToken(authentication);

        // when
        Authentication resultAuth = jwtTokenProvider.getAuthentication(token);

        // then
        assertThat(resultAuth.getName()).isEqualTo("testUser");
        assertThat(resultAuth.getAuthorities().stream().map(GrantedAuthority::getAuthority)).containsExactly(Role.GUARDIAN.name());
    }

    @Test
    @DisplayName("토큰에서 사용자 ID를 정상적으로 추출해야 한다.")
    void getUserIdFromToken_Success() {
        // given
        String token = jwtTokenProvider.createAccessToken(authentication);

        // when
        String userId = jwtTokenProvider.getUserIdFromToken(token);

        // then
        assertThat(userId).isEqualTo("testUser");
    }

    @Test
    @DisplayName("토큰의 만료 시간을 정상적으로 조회해야 한다.")
    void getExpiration_Success() {
        // given
        String token = jwtTokenProvider.createAccessToken(authentication);

        // when
        Long expiration = jwtTokenProvider.getExpiration(token);

        // then
        assertThat(expiration).isNotNull().isGreaterThan(0L);
    }
}
