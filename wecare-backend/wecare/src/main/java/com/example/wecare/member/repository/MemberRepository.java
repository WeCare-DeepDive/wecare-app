package com.example.wecare.member.repository;

import com.example.wecare.member.domain.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.id = :id")
    Optional<Member> findByIdWithPessimisticLock(Long id);

    @Query("SELECT m FROM Member m " +
           "LEFT JOIN FETCH m.guardianConnections gc " +
           "LEFT JOIN FETCH m.dependentConnections dc " +
           "WHERE m.id = :id")
    Optional<Member> findByIdWithConnections(@Param("id") Long id);
}