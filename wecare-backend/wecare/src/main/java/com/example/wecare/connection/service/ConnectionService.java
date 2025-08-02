package com.example.wecare.connection.service;

import com.example.wecare.common.code.AuthResponseCode;
import com.example.wecare.common.code.GeneralResponseCode;
import com.example.wecare.common.exception.ApiException;
import com.example.wecare.connection.domain.Connection;
import com.example.wecare.connection.dto.ConnectionDetailDto;
import com.example.wecare.connection.dto.ConnectionDto;
import com.example.wecare.connection.dto.UpdateRelationshipRequest;
import com.example.wecare.connection.repository.ConnectionRepository;
import com.example.wecare.member.code.Role;
import com.example.wecare.member.domain.Member;
import com.example.wecare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {
    private final MemberRepository memberRepository;
    private final ConnectionRepository connectionRepository;

    @Transactional(readOnly = true)
    public List<ConnectionDto> getMyConnections() {
        Member currentMember = getCurrentMember();

        // 활성화된 연결만 반환
        List<Connection> connections;
        if (currentMember.getRole() == Role.GUARDIAN) {
            connections = connectionRepository.findByGuardianAndActiveTrue(currentMember);
        } else {
            connections = connectionRepository.findByDependentAndActiveTrue(currentMember);
        }

        // DB 제약조건에 사용자 외래키 on delete cascade 적용되어 있으므로 별도 검증 과정 필요 없음

        return connections.stream().map(ConnectionDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<ConnectionDetailDto> getMyDetailConnections() {
        Member currentMember = getCurrentMember();

        List<ConnectionDto> connections = getMyConnections();
        List<ConnectionDetailDto> detailConnections = new ArrayList<>();
        for(ConnectionDto connectionDto : connections) {
            Member partner;
            if(currentMember.getRole() == Role.GUARDIAN) {
                partner = memberRepository.findById(connectionDto.getDependentId())
                        .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));
            } else {
                partner = memberRepository.findById(connectionDto.getGuardianId())
                        .orElseThrow(() -> new ApiException(AuthResponseCode.MEMBER_NOT_FOUND));
            }
            detailConnections.add(
                    ConnectionDetailDto.fromConnectionDto(connectionDto, partner)
            );
        }

        return detailConnections;
    }

    @PreAuthorize("@connectionAccessHandler.ownershipCheck(#connectionId)")
    @Transactional
    public void deactivateConnection(Long connectionId) {
        // 연결 해제하고자 하는 대상 사용자가 없으면 논리적으로 연결도 없으므로 Connection에 대한 NOT_FOUND로 간주
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.CONNECTION_NOT_FOUND));

        // 비활성화 된 연결의 경우 soft-deleted, 외부적으로 NOT_FOUND 반환
        if (!connection.isActive()) {
            throw new ApiException(GeneralResponseCode.CONNECTION_NOT_FOUND);
        }

        // Soft-Delete
        connection.setActive(false);
        connectionRepository.save(connection);
    }

    @PreAuthorize("@connectionAccessHandler.ownershipCheck(#connectionId)")
    @Transactional
    public void updateRelationship(Long connectionId, UpdateRelationshipRequest request) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ApiException(GeneralResponseCode.CONNECTION_NOT_FOUND));

        connection.setRelationshipType(request.getRelationshipType());
        connectionRepository.save(connection);
    }

    private Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Member) authentication.getPrincipal();
    }
}
