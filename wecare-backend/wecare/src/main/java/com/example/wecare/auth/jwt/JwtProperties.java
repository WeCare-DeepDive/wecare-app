package com.example.wecare.auth.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtProperties {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    @Value("${jwt.refresh-token-expiration-time}")
    private Long JWT_REFRESH_EXP;
    @Value("${jwt.access-token-expiration-time}")
    private Long JWT_ACCESS_EXP;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public long getAccessExp() {
        return JWT_ACCESS_EXP;
    }

    public long getRefreshExp() {
        return JWT_REFRESH_EXP;
    }
}