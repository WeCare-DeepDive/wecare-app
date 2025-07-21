package com.example.wecare.auth.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyGenerator {

    /**
     * HS256 알고리즘 기준으로 SecretKey를 Base64 문자열로 반환
     */
    public static String generateBase64SecretKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // or HS256
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static void main(String[] args) {
        String secret = generateBase64SecretKey();
        System.out.println("Generated Secret Key (Base64):\n" + secret);
    }
}
