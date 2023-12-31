package com.xlr8code.server.authentication.entity;

import com.xlr8code.server.common.utils.DateTimeUtils;
import com.xlr8code.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "user_activation_codes")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activation_code_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    public boolean isExpired() {
        return DateTimeUtils.isExpired(this.expiresAt);
    }

}
