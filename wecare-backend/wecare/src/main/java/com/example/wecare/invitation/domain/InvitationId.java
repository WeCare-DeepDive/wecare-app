package com.example.wecare.invitation.domain;

import java.io.Serializable;
import java.util.Objects;

public class InvitationId implements Serializable {

    private Long guardian;
    private Long dependent;

    public InvitationId() {
    }

    public InvitationId(Long guardian, Long dependent) {
        this.guardian = guardian;
        this.dependent = dependent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvitationId that = (InvitationId) o;
        return Objects.equals(guardian, that.guardian) &&
                Objects.equals(dependent, that.dependent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guardian, dependent);
    }
}
