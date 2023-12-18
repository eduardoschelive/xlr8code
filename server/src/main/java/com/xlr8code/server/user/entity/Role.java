package com.xlr8code.server.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Role {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true, columnDefinition = "SERIAL")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public SimpleGrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(this.name);
    }

}
