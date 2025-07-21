package com.example.wecare.invitation.service;

import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.redis.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @InjectMocks
    private InvitationService invitationService;

    @Mock
    private RedisService redisService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SecurityContext securityContext;

    private Member guardian;
    private Member dependent;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        guardian = new Member();
        guardian.setId(1L);
        guardian.setRole(Role.GUARDIAN);

        dependent = new Member();
        dependent.setId(2L);
        dependent.setRole(Role.DEPENDENT);
    }

    @Test
    @DisplayName("보호자가 초대 코드 생성 성공")
    void generateInvitationCode_success() {
        // given
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(guardian.getId().toString(), null));
        when(memberRepository.findById(guardian.getId())).thenReturn(Optional.of(guardian));

        // when
        String invitationCode = invitationService.generateInvitationCode();

        // then
        assertThat(invitationCode).isNotNull();
        assertThat(invitationCode.length()).isEqualTo(8);
        verify(redisService, times(1)).setValues(anyString(), eq(guardian.getId().toString()), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("피보호자가 초대 코드 생성 시도 시 실패")
    void generateInvitationCode_fail_whenDependentTries() {
        // given
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findById(dependent.getId())).thenReturn(Optional.of(dependent));

        // when & then
        assertThatThrownBy(() -> invitationService.generateInvitationCode())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("초대 코드는 보호자만 생성할 수 있습니다.");
    }

    @Test
    @DisplayName("피보호자가 유효한 코드로 수락 성공")
    void acceptInvitationCode_success() {
        // given
        String code = "VALID123";
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findById(dependent.getId())).thenReturn(Optional.of(dependent));
        when(redisService.getValues(anyString())).thenReturn(guardian.getId().toString());
        when(memberRepository.findById(guardian.getId())).thenReturn(Optional.of(guardian));

        // when
        invitationService.acceptInvitationCode(code);

        // then
        assertThat(dependent.getGuardian()).isEqualTo(guardian);
        verify(memberRepository, times(1)).save(dependent);
        verify(redisService, times(1)).deleteValues(anyString());
    }

    @Test
    @DisplayName("유효하지 않은 코드로 수락 시도 시 실패")
    void acceptInvitationCode_fail_withInvalidCode() {
        // given
        String code = "INVALID123";
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findById(dependent.getId())).thenReturn(Optional.of(dependent));
        when(redisService.getValues(anyString())).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(code))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 초대 코드입니다.");
    }

    @Test
    @DisplayName("이미 보호자와 연결된 피보호자가 수락 시도 시 실패")
    void acceptInvitationCode_fail_whenAlreadyConnected() {
        // given
        String code = "VALID123";
        dependent.setGuardian(new Member()); // 이미 다른 보호자와 연결된 상태
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findById(dependent.getId())).thenReturn(Optional.of(dependent));

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(code))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 보호자와 연결되어 있습니다.");
    }
}
