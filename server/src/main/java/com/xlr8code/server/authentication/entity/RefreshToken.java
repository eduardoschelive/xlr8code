package com.xlr8code.server.authentication.entity;

import com.xlr8code.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "token", nullable = false, updatable = false)
    private String token;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    @Column(name = "revoked_at")
    private Date revokedAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    public boolean isExpired() {
        return this.expiresAt.before(Date.from(Instant.now()));
    }

}
