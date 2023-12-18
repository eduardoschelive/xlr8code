package com.xlr8code.server.user.entity;

import com.xlr8code.server.user.utils.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Role {

    @Id
    @Column(name = "role_id", nullable = false, updatable = false, unique = true, columnDefinition = "SERIAL")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public SimpleGrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(this.userRole.getValue());
    }

}
