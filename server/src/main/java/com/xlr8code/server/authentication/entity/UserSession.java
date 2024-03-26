package com.xlr8code.server.authentication.entity;

import com.xlr8code.server.common.entity.AuditableEntity;
import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_sessions")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_session_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "session_token", nullable = false, unique = true)
    private String sessionToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public boolean isExpired() {
        return DateTimeUtils.isExpired(this.expiresAt);
    }

    public void refresh(String newSessionToken, Instant newExpiresAt) {
        this.sessionToken = newSessionToken;
        this.expiresAt = newExpiresAt;
    }

}
