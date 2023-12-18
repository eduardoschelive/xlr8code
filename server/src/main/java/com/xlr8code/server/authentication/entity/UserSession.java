package com.xlr8code.server.authentication.entity;

import com.xlr8code.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_session_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private UUID refreshToken;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    public UserSession(User user, Date expiresAt) {
        this.user = user;
        this.refreshToken = UUID.randomUUID();
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return this.expiresAt.before(Date.from(Instant.now()));
    }

    public void refresh() {
        this.refreshToken = UUID.randomUUID();
        this.expiresAt = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }

}
