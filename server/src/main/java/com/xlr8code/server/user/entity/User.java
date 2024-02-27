package com.xlr8code.server.user.entity;

import com.xlr8code.server.authentication.entity.UserCode;
import com.xlr8code.server.authentication.entity.UserSession;
import com.xlr8code.server.user.utils.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Embedded
    private UserPassword userPassword;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserMetadata metadata;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserPreferences preferences;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false, updatable = false)
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserSession> userSessions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserCode> userCodes;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public void activate() {
        this.active = true;
        this.activatedAt = Instant.now();
    }

    public Set<String> getNamedRoles() {
        return this.roles.stream().map(role -> role.getUserRole().getValue()).collect(Collectors.toSet());
    }

    public boolean hasRole(UserRole userRole) {
        return this.roles.stream().anyMatch(role -> role.getUserRole().equals(userRole));
    }

    public boolean isAdmin() {
        return this.hasRole(UserRole.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles().stream().map(Role::toGrantedAuthority).collect(Collectors.toList());
    }

    @Override
    public boolean isEnabled() {
        return this.isActive();
    }

    @Override
    public String getPassword() {
        return this.userPassword.getEncodedPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
