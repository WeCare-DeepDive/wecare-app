package com.example.wecare.member.service;

import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.domain.Role;
import com.example.wecare.member.dto.MemberResponse;
import com.example.wecare.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SecurityContext securityContext;

    private Member guardian1;
    private Member guardian2;
    private Member dependent1;
    private Member dependent2;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        guardian1 = Member.builder().id(1L).username("guardian1").name("보호자1").role(Role.GUARDIAN).birthDate(LocalDate.of(1980, 1, 1)).build();
        guardian2 = Member.builder().id(2L).username("guardian2").name("보호자2").role(Role.GUARDIAN).birthDate(LocalDate.of(1985, 5, 5)).build();
        dependent1 = Member.builder().id(3L).username("dependent1").name("피보호자1").role(Role.DEPENDENT).birthDate(LocalDate.of(2000, 10, 10)).build();
        dependent2 = Member.builder().id(4L).username("dependent2").name("피보호자2").role(Role.DEPENDENT).birthDate(LocalDate.of(2005, 12, 12)).build();

        // 관계 설정
        // guardian1 -> dependent1
        Invitation conn1 = new Invitation();
        conn1.setGuardian(guardian1);
        conn1.setDependent(dependent1);
        Set<Invitation> g1_dep_conns = new HashSet<>();
        g1_dep_conns.add(conn1);
        guardian1.setDependentConnections(g1_dep_conns);
        Set<Invitation> d1_gua_conns = new HashSet<>();
        d1_gua_conns.add(conn1);
        dependent1.setGuardianConnections(d1_gua_conns);

        // guardian1 -> dependent2
        Invitation conn2 = new Invitation();
        conn2.setGuardian(guardian1);
        conn2.setDependent(dependent2);
        g1_dep_conns.add(conn2);
        guardian1.setDependentConnections(g1_dep_conns);
        Set<Invitation> d2_gua_conns = new HashSet<>();
        d2_gua_conns.add(conn2);
        dependent2.setGuardianConnections(d2_gua_conns);

        // guardian2 -> dependent1 (dependent1이 여러 보호자를 가질 수 있음을 테스트)
        Invitation conn3 = new Invitation();
        conn3.setGuardian(guardian2);
        conn3.setDependent(dependent1);
        Set<Invitation> g2_dep_conns = new HashSet<>();
        g2_dep_conns.add(conn3);
        guardian2.setDependentConnections(g2_dep_conns);
        d1_gua_conns.add(conn3);
        dependent1.setGuardianConnections(d1_gua_conns);
    }

    private void mockCurrentUser(Member member) {
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(member.getId().toString(), null));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
    }

    @Test
    @DisplayName("성공: 현재 로그인된 사용자 정보 조회")
    void getMe_Success() {
        // given
        mockCurrentUser(guardian1);

        // when
        MemberResponse response = memberService.getMe();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(guardian1.getId());
        assertThat(response.getUsername()).isEqualTo(guardian1.getUsername());
        assertThat(response.getName()).isEqualTo(guardian1.getName());
    }

    @Test
    @DisplayName("성공: 보호자가 자신의 피보호자 목록 조회")
    void getMyDependents_Success_Guardian() {
        // given
        mockCurrentUser(guardian1);

        // when
        List<MemberResponse> dependents = memberService.getMyDependents();

        // then
        assertThat(dependents).hasSize(2);
        assertThat(dependents.stream().map(MemberResponse::getId).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(dependent1.getId(), dependent2.getId());
    }

    @Test
    @DisplayName("실패: 피보호자가 피보호자 목록 조회 시도")
    void getMyDependents_Fail_DependentTries() {
        // given
        mockCurrentUser(dependent1);

        // when & then
        assertThatThrownBy(() -> memberService.getMyDependents())
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("보호자만 피보호자 목록을 조회할 수 있습니다.");
    }

    @Test
    @DisplayName("성공: 피보호자 정보 조회 시 첫 번째 보호자 ID 포함")
    void convertToResponse_DependentWithGuardianId() {
        // given
        mockCurrentUser(dependent1);

        // when
        MemberResponse response = memberService.getMe(); // getMe를 통해 convertToResponse 호출

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(dependent1.getId());
        // dependent1은 guardian1과 guardian2 모두와 연결되어 있으므로, 첫 번째 보호자의 ID가 반환되는지 확인
        // HashSet의 순서는 보장되지 않으므로, 두 보호자 중 하나가 반환되는지 확인
        assertThat(response.getGuardianId()).isIn(guardian1.getId(), guardian2.getId());
    }

    @Test
    @DisplayName("성공: 보호자 정보 조회 시 guardianId는 null")
    void convertToResponse_GuardianWithoutGuardianId() {
        // given
        mockCurrentUser(guardian1);

        // when
        MemberResponse response = memberService.getMe();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(guardian1.getId());
        assertThat(response.getGuardianId()).isNull();
    }
}
