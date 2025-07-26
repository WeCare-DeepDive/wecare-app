package com.example.wecare.unit.service;

import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.invitation.domain.InvitationId;
import com.example.wecare.invitation.repository.InvitationRepository;
import com.example.wecare.invitation.service.InvitationService;
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

    private Member guardian;
    private Member dependent;
    private Member otherGuardian;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        guardian = Member.builder().id(1L).role(Role.GUARDIAN).build();
        dependent = Member.builder().id(2L).role(Role.DEPENDENT).build();
        otherGuardian = Member.builder().id(3L).role(Role.GUARDIAN).build();
    }

    private void mockAuthentication(Member member) {
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(member.getId().toString(), null));
    }

    private void mockMemberFindById(Member member) {
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
    }

    private void mockMemberFindByIdWithPessimisticLock(Member member) {
        when(memberRepository.findByIdWithPessimisticLock(member.getId())).thenReturn(Optional.of(member));
    }

    @Test
    @DisplayName("역할에 관계없이 초대 코드 생성 성공")
    void generateInvitationCode_byAnyRole_success() {
        // given
        mockAuthentication(guardian);
        mockMemberFindById(guardian);

        // when
        String invitationCode = invitationService.generateInvitationCode();

        // then
        assertThat(invitationCode).isNotNull();
        assertThat(invitationCode.length()).isEqualTo(8);
        verify(redisService, times(1)).setValues(anyString(), eq(guardian.getId().toString()), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("피보호자가 보호자의 코드를 수락하여 연결 성공")
    void acceptInvitationCode_dependentAcceptsGuardianCode_success() {
        // given
        String validCode = "VALID123";
        mockAuthentication(dependent);
        mockMemberFindByIdWithPessimisticLock(dependent);
        mockMemberFindByIdWithPessimisticLock(guardian);

        when(redisService.getValues("INVITE:" + validCode)).thenReturn(guardian.getId().toString());
        when(invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(anyLong(), anyLong())).thenReturn(false);

        // when
        invitationService.acceptInvitationCode(validCode);

        // then
        ArgumentCaptor<Invitation> invitationCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository, times(1)).save(invitationCaptor.capture());
        Invitation savedInvitation = invitationCaptor.getValue();

        assertThat(savedInvitation.getGuardian()).isEqualTo(guardian);
        assertThat(savedInvitation.getDependent()).isEqualTo(dependent);
        assertThat(savedInvitation.isActive()).isTrue(); // 활성화 상태 확인
        verify(redisService, times(1)).deleteValues("INVITE:" + validCode);
    }
    
    @Test
    @DisplayName("보호자가 피보호자의 코드를 수락하여 연결 성공")
    void acceptInvitationCode_guardianAcceptsDependentCode_success() {
        // given
        String validCode = "VALID456";
        mockAuthentication(guardian);
        mockMemberFindByIdWithPessimisticLock(guardian);
        mockMemberFindByIdWithPessimisticLock(dependent);

        when(redisService.getValues("INVITE:" + validCode)).thenReturn(dependent.getId().toString());
        when(invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(anyLong(), anyLong())).thenReturn(false);

        // when
        invitationService.acceptInvitationCode(validCode);

        // then
        ArgumentCaptor<Invitation> invitationCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository, times(1)).save(invitationCaptor.capture());
        Invitation savedInvitation = invitationCaptor.getValue();

        assertThat(savedInvitation.getGuardian()).isEqualTo(guardian);
        assertThat(savedInvitation.getDependent()).isEqualTo(dependent);
        assertThat(savedInvitation.isActive()).isTrue(); // 활성화 상태 확인
        verify(redisService, times(1)).deleteValues("INVITE:" + validCode);
    }

    @Test
    @DisplayName("유효하지 않은 코드로 수락 시도 시 실패")
    void acceptInvitationCode_fail_withInvalidCode() {
        // given
        String invalidCode = "INVALID123";
        mockAuthentication(dependent);
        mockMemberFindByIdWithPessimisticLock(dependent);

        when(redisService.getValues("INVITE:" + invalidCode)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(invalidCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 초대 코드입니다.");
    }

    @Test
    @DisplayName("이미 활성화된 연결이 존재할 경우 수락 시도 시 실패")
    void acceptInvitationCode_fail_whenAlreadyActiveConnected() {
        // given
        String validCode = "VALID123";
        mockAuthentication(dependent);
        mockMemberFindByIdWithPessimisticLock(dependent);
        mockMemberFindByIdWithPessimisticLock(guardian);

        when(redisService.getValues("INVITE:" + validCode)).thenReturn(guardian.getId().toString());
        when(invitationRepository.existsByGuardianIdAndDependentIdAndIsActiveTrue(anyLong(), anyLong())).thenReturn(true); // 활성화된 연결이 존재한다고 모의

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
        mockAuthentication(guardian);
        mockMemberFindByIdWithPessimisticLock(guardian);
        mockMemberFindByIdWithPessimisticLock(otherGuardian);

        when(redisService.getValues("INVITE:" + validCode)).thenReturn(otherGuardian.getId().toString());

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
        mockAuthentication(guardian);
        mockMemberFindByIdWithPessimisticLock(guardian);

        when(redisService.getValues("INVITE:" + ownCode)).thenReturn(guardian.getId().toString());

        // when & then
        assertThatThrownBy(() -> invitationService.acceptInvitationCode(ownCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자신의 초대 코드는 수락할 수 없습니다.");
    }

    @Test
    @DisplayName("연결 삭제 성공 - isActive가 false로 변경되어야 한다.")
    void deleteConnection_success() {
        // given
        mockAuthentication(guardian);
        mockMemberFindByIdWithPessimisticLock(guardian);
        mockMemberFindByIdWithPessimisticLock(dependent);

        Invitation existingInvitation = Invitation.builder()
                .guardian(guardian)
                .dependent(dependent)
                .isActive(true)
                .build();

        when(invitationRepository.findById(any(InvitationId.class))).thenReturn(Optional.of(existingInvitation));

        // when
        invitationService.deleteConnection(dependent.getId());

        // then
        ArgumentCaptor<Invitation> invitationCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository, times(1)).save(invitationCaptor.capture());
        Invitation savedInvitation = invitationCaptor.getValue();

        assertThat(savedInvitation.isActive()).isFalse();
    }

    @Test
    @DisplayName("연결 삭제 실패 - 존재하지 않는 연결")
    void deleteConnection_fail_notExist() {
        // given
        mockAuthentication(guardian);
        mockMemberFindByIdWithPessimisticLock(guardian);
        mockMemberFindByIdWithPessimisticLock(dependent);

        when(invitationRepository.findById(any(InvitationId.class))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> invitationService.deleteConnection(dependent.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 연결입니다.");
    }

    @Test
    @DisplayName("연결 삭제 실패 - 이미 비활성화된 연결")
    void deleteConnection_fail_alreadyInactive() {
        // given
        mockAuthentication(guardian);
        mockMemberFindByIdWithPessimisticLock(guardian);
        mockMemberFindByIdWithPessimisticLock(dependent);

        Invitation existingInvitation = Invitation.builder()
                .guardian(guardian)
                .dependent(dependent)
                .isActive(false) // 이미 비활성화된 상태
                .build();

        when(invitationRepository.findById(any(InvitationId.class))).thenReturn(Optional.of(existingInvitation));

        // when & then
        assertThatThrownBy(() -> invitationService.deleteConnection(dependent.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 비활성화된 연결입니다.");
    }
}
