package com.example.wecare.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityUtilTest {

    private SecurityContext originalSecurityContext;

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 원래 SecurityContext를 저장
        originalSecurityContext = SecurityContextHolder.getContext();
        // 새로운 SecurityContext 생성
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    }

    @AfterEach
    void tearDown() {
        // 각 테스트 후에 원래 SecurityContext로 복원
        SecurityContextHolder.setContext(originalSecurityContext);
    }

    @Test
    @DisplayName("SecurityContext에 유효한 사용자 ID가 있을 때 올바른 ID를 반환해야 한다.")
    void getCurrentMemberId_Success() {
        // Given
        Long expectedMemberId = 123L;
        Authentication authentication = new UsernamePasswordAuthenticationToken(expectedMemberId.toString(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        Long actualMemberId = SecurityUtil.getCurrentMemberId();

        // Then
        assertEquals(expectedMemberId, actualMemberId);
    }

    @Test
    @DisplayName("SecurityContext에 인증 정보가 없을 때 RuntimeException을 발생해야 한다.")
    void getCurrentMemberId_NoAuthentication() {
        // Given: SecurityContext에 인증 정보가 없음 (setUp에서 이미 처리됨)

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> SecurityUtil.getCurrentMemberId());
        assertEquals("Security Context에 인증 정보가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Authentication.getName()이 null일 때 RuntimeException을 발생해야 한다.")
    void getCurrentMemberId_NameIsNull() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> SecurityUtil.getCurrentMemberId());
        assertEquals("Security Context에 인증 정보가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Authentication.getName()이 숫자가 아닌 문자열일 때 RuntimeException을 발생해야 한다.")
    void getCurrentMemberId_NameIsNotNumber() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("notANumber", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> SecurityUtil.getCurrentMemberId());
        assertEquals("인증 정보가 올바르지 않습니다.", exception.getMessage());
        assertTrue(exception.getCause() instanceof NumberFormatException);
    }

    @Test
    @DisplayName("Authentication.getName()이 빈 문자열일 때 RuntimeException을 발생해야 한다.")
    void getCurrentMemberId_NameIsEmptyString() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> SecurityUtil.getCurrentMemberId());
        assertEquals("인증 정보가 올바르지 않습니다.", exception.getMessage());
        assertTrue(exception.getCause() instanceof NumberFormatException);
    }
}
