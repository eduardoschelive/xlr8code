package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import jakarta.validation.constraints.NotNull;

public record UpdateUserPreferencesDTO(
        @NotNull
        Language language,
        @NotNull
        Theme theme
) {
}
