package com.example.wecare.unit;

import com.example.wecare.member.code.Gender;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.dto.PartnerResponse;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;


    // 가짜 Authentication 객체 생성
    Authentication auth;

    // SecurityContext 모킹
    SecurityContext context;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(3L)
                .username("testUser1234")
                .password("testPassword1234")
                .birthDate(LocalDate.of(1950, 3, 3))
                .name("testName")
                .role(Role.GUARDIAN)
                .gender(Gender.FEMALE)
                .build();

        auth = Mockito.mock(Authentication.class);
        context = Mockito.mock(SecurityContext.class);

        // SecurityContextHolder에 주입
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("[MemberService][Unit] 현재 사용자 정보 조회 Success")
    void getMeTestSuccess() {
        //given
        when(auth.getPrincipal()).thenReturn(testMember);
        when(context.getAuthentication()).thenReturn(auth);

        //when
        MemberResponse memberResponse = memberService.getMe();

        //then
        assertEquals(testMember.getUsername(), memberResponse.getUsername());
        assertEquals(testMember.getGender(), memberResponse.getGender());
        assertEquals(testMember.getBirthDate(), memberResponse.getBirthDate());
        assertEquals(testMember.getRole(), memberResponse.getRole());
    }

    @Test
    @DisplayName("[MemberService][Unit] 연결된 사용자된정보 조회 Success")
    void getPartnerTestSuccess() {
        //given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testMember));

        //when
        PartnerResponse memberResponse = memberService.getPartner(anyLong());

        //then
        assertEquals(testMember.getUsername(), memberResponse.getUsername());
        assertEquals(testMember.getGender(), memberResponse.getGender());
        assertEquals(testMember.getRole(), memberResponse.getRole());
    }
}
