package com.example.wecare.invitation.service;

import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.invitation.domain.InvitationId;
import com.example.wecare.invitation.repository.InvitationRepository;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.repository.MemberRepository;
import com.example.wecare.redis.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private InvitationRepository invitationRepository;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("역할에 관계없이 초대 코드 생성 성공")
    void generateInvitationCode_byAnyRole_success() {
        // given
        Member user = Member.builder().id(1L).role(Role.GUARDIAN).build();
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(user.getId().toString(), null));
        when(memberRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        String invitationCode = invitationService.generateInvitationCode();

        // then
        assertThat(invitationCode).isNotNull();
        assertThat(invitationCode.length()).isEqualTo(8);
        verify(redisService, times(1)).setValues(anyString(), eq(user.getId().toString()), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("피보호자가 보호자의 코드를 수락하여 연결 성공")
    void acceptInvitationCode_dependentAcceptsGuardianCode_success() {
        // given
        String validCode = "VALID123";
        Member guardian = Member.builder().id(1L).role(Role.GUARDIAN).build();
        Member dependent = Member.builder().id(2L).role(Role.DEPENDENT).build();

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findByIdWithPessimisticLock(dependent.getId())).thenReturn(Optional.of(dependent));
        when(memberRepository.findByIdWithPessimisticLock(guardian.getId())).thenReturn(Optional.of(guardian));
        when(redisService.getValues("INVITE:" + validCode)).thenReturn(guardian.getId().toString());
        when(invitationRepository.existsById(any(InvitationId.class))).thenReturn(false);

        // when
        invitationService.acceptInvitationCode(validCode);

        // then
        ArgumentCaptor<Invitation> invitationCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository, times(1)).save(invitationCaptor.capture());
        Invitation savedInvitation = invitationCaptor.getValue();

        assertThat(savedInvitation.getGuardian()).isEqualTo(guardian);
        assertThat(savedInvitation.getDependent()).isEqualTo(dependent);
        verify(redisService, times(1)).deleteValues("INVITE:" + validCode);
    }
    
    @Test
    @DisplayName("보호자가 피보호자의 코드를 수락하여 연결 성공")
    void acceptInvitationCode_guardianAcceptsDependentCode_success() {
        // given
        String validCode = "VALID456";
        Member guardian = Member.builder().id(1L).role(Role.GUARDIAN).build();
        Member dependent = Member.builder().id(2L).role(Role.DEPENDENT).build();

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(guardian.getId().toString(), null));
        when(memberRepository.findByIdWithPessimisticLock(guardian.getId())).thenReturn(Optional.of(guardian));
        when(memberRepository.findByIdWithPessimisticLock(dependent.getId())).thenReturn(Optional.of(dependent));
        when(redisService.getValues("INVITE:" + validCode)).thenReturn(dependent.getId().toString());
        when(invitationRepository.existsById(any(InvitationId.class))).thenReturn(false);

        // when
        invitationService.acceptInvitationCode(validCode);

        // then
        ArgumentCaptor<Invitation> invitationCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository, times(1)).save(invitationCaptor.capture());
        Invitation savedInvitation = invitationCaptor.getValue();

        assertThat(savedInvitation.getGuardian()).isEqualTo(guardian);
        assertThat(savedInvitation.getDependent()).isEqualTo(dependent);
        verify(redisService, times(1)).deleteValues("INVITE:" + validCode);
    }

    @Test
    @DisplayName("유효하지 않은 코드로 수락 시도 시 실패")
    void acceptInvitationCode_fail_withInvalidCode() {
        // given
        String invalidCode = "INVALID123";
        Member dependent = Member.builder().id(2L).role(Role.DEPENDENT).build();
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findByIdWithPessimisticLock(dependent.getId())).thenReturn(Optional.of(dependent));
        when(redisService.getValues("INVITE:" + invalidCode)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(invalidCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 초대 코드입니다.");
    }

    @Test
    @DisplayName("이미 연결된 관계일 경우 수락 시도 시 실패")
    void acceptInvitationCode_fail_whenAlreadyConnected() {
        // given
        String validCode = "VALID123";
        Member guardian = Member.builder().id(1L).role(Role.GUARDIAN).build();
        Member dependent = Member.builder().id(2L).role(Role.DEPENDENT).build();

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(dependent.getId().toString(), null));
        when(memberRepository.findByIdWithPessimisticLock(dependent.getId())).thenReturn(Optional.of(dependent));
        when(memberRepository.findByIdWithPessimisticLock(guardian.getId())).thenReturn(Optional.of(guardian));
        when(redisService.getValues("INVITE:" + validCode)).thenReturn(guardian.getId().toString());
        when(invitationRepository.existsById(new InvitationId(guardian.getId(), dependent.getId()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(validCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 연결된 관계입니다.");
    }
    
    @Test
    @DisplayName("동일한 역할끼리 연결 시도 시 실패")
    void acceptInvitationCode_fail_withSameRole() {
        // given
        String validCode = "VALID789";
        Member guardian1 = Member.builder().id(1L).role(Role.GUARDIAN).build();
        Member guardian2 = Member.builder().id(3L).role(Role.GUARDIAN).build();

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(guardian1.getId().toString(), null));
        when(memberRepository.findByIdWithPessimisticLock(guardian1.getId())).thenReturn(Optional.of(guardian1));
        when(memberRepository.findByIdWithPessimisticLock(guardian2.getId())).thenReturn(Optional.of(guardian2));
        when(redisService.getValues("INVITE:" + validCode)).thenReturn(guardian2.getId().toString());

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(validCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역할의 사용자와는 연결할 수 없습니다.");
    }

    @Test
    @DisplayName("자신의 초대 코드를 수락 시도 시 실패")
    void acceptInvitationCode_fail_withOwnCode() {
        // given
        String ownCode = "OWNCODE1";
        Member user = Member.builder().id(1L).role(Role.GUARDIAN).build();

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(user.getId().toString(), null));
        when(memberRepository.findByIdWithPessimisticLock(user.getId())).thenReturn(Optional.of(user));
        when(redisService.getValues("INVITE:" + ownCode)).thenReturn(user.getId().toString());

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(ownCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자신의 초대 코드는 수락할 수 없습니다.");
    }
}
