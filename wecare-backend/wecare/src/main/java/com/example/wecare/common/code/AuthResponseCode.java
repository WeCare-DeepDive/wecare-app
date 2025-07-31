package com.example.wecare.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AuthResponseCode implements ApiResponseCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    INVALID_USERNAME(HttpStatus.NOT_FOUND, "유효하지 않은 계정입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호입니다."),
    JOIN_OK(HttpStatus.OK, "회원가입에 성공했습니다."),
    LOGOUT_OK(HttpStatus.OK, "로그아웃에 성공했습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "로그아웃에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인가되지 않은 접근입니다."),
    LOGIN_OK(HttpStatus.OK, "로그인에 성공했습니다.");

    private final HttpStatus status;
    private final String message;
}
