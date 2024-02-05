package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.annotation.NullOrNotBlank;

public record UpdateUserDTO(
        @NullOrNotBlank
        String username,
        @NullOrNotBlank
        String email,
        String currentPassword,
        String newPassword
) {
}
