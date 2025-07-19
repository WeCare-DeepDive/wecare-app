package com.example.wecare.auth.service;

import com.example.wecare.member.domain.Gender;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.auth.dto.SignUpRequest;
import com.example.wecare.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("정상적인 정보로 회원가입을 성공하고 비밀번호는 암호화되어야 한다.")
    void signUp_Success() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setPassword("password123");
        request.setName("테스트");
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        request.setPhone("010-1234-5678");
        request.setRole(Role.GUARDIAN);

        // when
        authService.signUp(request);

        // then
        Member foundMember = memberRepository.findByMemberId("010-1234-5678").orElse(null);
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo("테스트");
        assertThat(foundMember.getRole()).isEqualTo(Role.GUARDIAN);
        assertThat(passwordEncoder.matches("password123", foundMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복된 전화번호로 회원가입을 시도하면 예외가 발생해야 한다.")
    void signUp_Fail_DuplicatePhone() {
        // given
        SignUpRequest request1 = new SignUpRequest();
        request1.setPassword("password123");
        request1.setName("사용자1");
        request1.setGender(Gender.MALE);
        request1.setBirthDate(LocalDate.of(1990, 1, 1));
        request1.setPhone("010-8888-8888");
        request1.setRole(Role.GUARDIAN);
        authService.signUp(request1);

        SignUpRequest request2 = new SignUpRequest();
        request2.setPassword("password456");
        request2.setName("사용자2");
        request2.setGender(Gender.FEMALE);
        request2.setBirthDate(LocalDate.of(1995, 2, 2));
        request2.setPhone("010-8888-8888");
        request2.setRole(Role.DEPENDENT);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signUp(request2);
        });
        assertThat(exception.getMessage()).isEqualTo("이미 등록된 전화번호입니다.");
    }
}
