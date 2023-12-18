package com.xlr8code.server.user.utils;

import com.xlr8code.server.user.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AvailableRoles {

    ROLE_MEMBER(1L, "ROLE_MEMBER"),
    ROLE_ADMIN(2L, "ROLE_ADMIN");

    private final Long id;
    private final String value;

    public Role toRole() {
        return Role.builder().id(this.id).roleName(this).build();
    }

}
