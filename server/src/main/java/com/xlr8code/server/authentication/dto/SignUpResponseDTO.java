package com.xlr8code.server.authentication.dto;

import com.xlr8code.server.user.entity.Role;

import java.util.Set;
import java.util.UUID;

public record SignUpResponseDTO(
        UUID uuid,
        String username,
        String email,
        boolean active,
        Set<String> roles
) {
}
