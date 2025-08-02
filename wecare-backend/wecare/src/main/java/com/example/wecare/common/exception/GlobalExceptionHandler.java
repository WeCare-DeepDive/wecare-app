package com.example.wecare.common.exception;

import com.example.wecare.common.code.AuthResponseCode;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialException(
            BadCredentialsException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponseCode.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedException(
            AccessDeniedException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AuthResponseCode.FORBIDDEN.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> methodArgumentNotValidException(
            final MethodArgumentNotValidException e, final HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        HashMap<String, String> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, String>> httpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}", e, request.getRequestURI(), e.getMessage());

        HashMap<String, String> error = new HashMap<>();

        if (e.getCause() instanceof UnrecognizedPropertyException cause) {
            String propertyName = cause.getPropertyName();

            error.put("code", "UNKNOWN_FIELD");
            error.put("message", String.format("요청에 알 수 없는 필드 '%s'가 포함되어 있습니다.", propertyName));
        } else {
            error.put("code", "INVALID_JSON");
            error.put("message", "요청 본문이 올바른 JSON 형식인지 확인해주세요.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> apiException(
            ApiException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e, request.getRequestURI(), e.getMessage());

        //String errorMessage = e.getMessage().length() > 20 ? e.getMessage().substring(0, 100) + " ..." : e.getMessage();

        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
