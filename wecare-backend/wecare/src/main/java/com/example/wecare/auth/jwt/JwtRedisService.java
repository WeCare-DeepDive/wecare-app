package com.example.wecare.auth.jwt;

import com.example.wecare.common.code.AuthResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtRedisService {
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private static final String LOGOUT_PREFIX = "logout:";
    private static final String WITHDRAWN_PREFIX = "withdraw:";
    private static final String REFRESH_PREFIX = "refresh:";
    @Value("${jwt.access-token-expiration-time}")
    private Long accessExp;
    @Value("${jwt.refresh-token-expiration-time}")
    private Long refreshExp;

    public void logoutToken(String token) {
        long remain = jwtUtil.getExpirationFromToken(token).getTime() - System.currentTimeMillis();

        if (isTokenLogout(token)) {
            throw new ApiException(AuthResponseCode.INVALID_TOKEN);
        }

        redisService.setValues(
                LOGOUT_PREFIX + token,
                "true",
                remain
        );
    }

    public void saveRefreshToken(String refreshToken) {
        if (isTokenLogout(refreshToken)) {
            throw new ApiException(AuthResponseCode.INVALID_TOKEN);
        }

        redisService.setValues(
                REFRESH_PREFIX + refreshToken,
                "true",
                refreshExp
        );
    }

    public void deleteRefreshToken(String refreshToken) {
        redisService.deleteValues(REFRESH_PREFIX + refreshToken);
    }

    public void withdrawToken(String token) {
        if (isTokenLogout(token)) {
            throw new ApiException(AuthResponseCode.INVALID_TOKEN);
        }

        redisService.setValues(
                WITHDRAWN_PREFIX + jwtUtil.getIdFromToken(token),
                "true",
                accessExp
        );
    }

    public boolean isTokenLogout(String token) {
        return redisService.hasKey(LOGOUT_PREFIX + token);
    }

    public boolean hasRefreshToken(String token) {
        return redisService.hasKey(REFRESH_PREFIX + token);
    }

    public boolean isTokenWithdrawn(String token) {
        return redisService.hasKey(WITHDRAWN_PREFIX + jwtUtil.getIdFromToken(token));
    }
}