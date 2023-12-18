package com.xlr8code.server.authentication.entity;

import com.xlr8code.server.common.entity.Auditable;
import com.xlr8code.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "verification_tokens")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class VerificationToken extends Auditable {

    @Transient
    private static final int DEFAULT_EXPIRATION = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    public static VerificationToken generate(User user) {

        var token = UUID.randomUUID().toString();
        System.out.println(token);

        return VerificationToken.builder()
                .token(token)
                .expiresAt(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION * 60 * 1000))
                .user(user)
                .build();
    }

    public boolean isExpired() {
        return this.expiresAt.before(new Date());
    }

}
