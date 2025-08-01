package com.example.wecare.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
}
