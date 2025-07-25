package com.example.wecare.invitation.repository;

import com.example.wecare.invitation.domain.Invitation;
import com.example.wecare.invitation.domain.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {
}
