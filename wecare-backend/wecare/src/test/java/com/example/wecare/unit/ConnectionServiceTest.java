package com.example.wecare.unit;

import com.example.wecare.connection.code.RelationshipType;
import com.example.wecare.connection.domain.Connection;
import com.example.wecare.connection.dto.ConnectionDto;
import com.example.wecare.connection.dto.UpdateRelationshipRequest;
import com.example.wecare.connection.repository.ConnectionRepository;
import com.example.wecare.connection.service.ConnectionService;
import com.example.wecare.member.code.Gender;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {
    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;


    // 가짜 Authentication 객체 생성
    Authentication auth;

    // SecurityContext 모킹
    SecurityContext context;

    private Member testMember;
    private Member testTargetMember;
    private Connection testConnection;

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

        testTargetMember = Member.builder()
                .id(3L)
                .username("testUser4321")
                .password("testPassword1234")
                .birthDate(LocalDate.of(1970, 3, 3))
                .name("testName")
                .role(Role.DEPENDENT)
                .gender(Gender.FEMALE)
                .build();

        testConnection = Connection.builder()
                .id(2L)
                .active(true)
                .guardian(testMember)
                .dependent(testTargetMember)
                .relationshipType(RelationshipType.GRANDPARENT)
                .build();

        auth = Mockito.mock(Authentication.class);
        context = Mockito.mock(SecurityContext.class);

        // SecurityContextHolder에 주입
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("[ConnectionService][Unit] 현재 사용자 연결 목록 조회 Success")
    void getMyConnectionsTestSuccess() {
        //given
        when(connectionRepository.findByGuardianAndActiveTrue(testMember))
                .thenReturn(List.of(testConnection));
        when(connectionRepository.findByDependentAndActiveTrue(testTargetMember))
                .thenReturn(List.of(testConnection));
        when(auth.getPrincipal()).thenReturn(testMember);
        when(context.getAuthentication()).thenReturn(auth);

        //when
        List<ConnectionDto> connections = connectionService.getMyConnections();

        when(auth.getPrincipal()).thenReturn(testTargetMember);
        List<ConnectionDto> connectionsDependent = connectionService.getMyConnections();

        //then
        assertEquals(1, connections.size());
        assertEquals(testConnection.getId(), connections.get(0).getId());
        assertEquals(testConnection.getGuardian().getId(), connections.get(0).getGuardianId());
        assertEquals(testConnection.getDependent().getId(), connections.get(0).getDependentId());
        assertEquals(1, connectionsDependent.size());
        assertEquals(testConnection.getId(), connectionsDependent.get(0).getId());
        assertEquals(testConnection.getGuardian().getId(), connectionsDependent.get(0).getGuardianId());
        assertEquals(testConnection.getDependent().getId(), connectionsDependent.get(0).getDependentId());
    }

    @Test
    @DisplayName("[ConnectionService][Unit] 연결 비활성화 Success")
    void deactivateConnectionTestSuccess() {
        //given
        when(connectionRepository.findById(testConnection.getId()))
                .thenReturn(Optional.of(testConnection));

        //when
        connectionService.deactivateConnection(testConnection.getId());

        //then
        assertFalse(testConnection.isActive());
    }

    @Test
    @DisplayName("[ConnectionService][Unit] 관계 업데이트 Success")
    void updateRelationshipTestSuccess() {
        //given
        UpdateRelationshipRequest updateRequest = UpdateRelationshipRequest.builder()
                .relationshipType(RelationshipType.OTHER)
                .build();
        when(connectionRepository.findById(testConnection.getId()))
                .thenReturn(Optional.of(testConnection));

        //when
        connectionService.updateRelationship(testConnection.getId(), updateRequest);

        //then
        assertEquals(RelationshipType.OTHER, testConnection.getRelationshipType());
    }
}
