package com.example.wecare.connection.repository;

import com.example.wecare.connection.domain.Connection;
import com.example.wecare.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    boolean existsByGuardianIdAndDependentIdAndActiveTrue(Long guardianId, Long dependentId);

    Optional<Connection> findByGuardianAndDependent(Member guardian, Member dependent);

    List<Connection> findByGuardianAndActiveTrue(Member guardian);

    List<Connection> findByDependentAndActiveTrue(Member dependent);
}