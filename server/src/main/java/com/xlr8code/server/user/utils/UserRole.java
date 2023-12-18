package com.xlr8code.server.user.utils;

import com.xlr8code.server.user.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

    MEMBER(1L, "ROLE_MEMBER"),
    ADMIN(2L, "ROLE_ADMIN");

    private final Long id;
    private final String value;

    public Role toRole() {
        return Role.builder().id(this.id).userRole(this).build();
    }

}
