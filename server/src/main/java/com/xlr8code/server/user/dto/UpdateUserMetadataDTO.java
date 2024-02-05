package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.annotation.NullOrNotBlank;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import jakarta.validation.constraints.NotNull;

public record UpdateUserMetadataDTO(
        @NotNull
        Theme themePreference,
        @NotNull
        Language languagePreference,
        @NullOrNotBlank
        String profilePictureUrl
) {
}
