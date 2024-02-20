package com.xlr8code.server.user.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateUserMetadataDTO(
        @NotNull
        String profilePictureUrl
) {
}
