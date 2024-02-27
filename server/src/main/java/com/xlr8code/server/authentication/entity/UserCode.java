package com.xlr8code.server.authentication.entity;

import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_codes")
@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code_type", nullable = false, updatable = false)
    private UserCodeType codeType;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public boolean isExpired() {
        return DateTimeUtils.isExpired(this.expiresAt);
    }

}
