package com.example.wecare.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.member.domain.Gender;
import com.example.wecare.member.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유효한 정보로 회원가입을 요청하면 성공(200 OK)해야 한다.")
    void signUp_Success() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setPassword("password123");
        request.setName("테스트");
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        request.setPhone("010-1234-5678");
        request.setRole(Role.GUARDIAN);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 전화번호 형식으로 회원가입을 요청하면 실패(400 Bad Request)해야 한다.")
    void signUp_Fail_InvalidPhoneNumber() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setPassword("password123");
        request.setName("테스트");
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        request.setPhone("01012345678"); // Invalid format
        request.setRole(Role.GUARDIAN);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("필수 필드(이름)가 누락된 채로 회원가입을 요청하면 실패(400 Bad Request)해야 한다.")
    void signUp_Fail_MissingName() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setPassword("password123");
        // request.setName("테스트"); // Missing name
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        request.setPhone("010-1234-5678");
        request.setRole(Role.GUARDIAN);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
