package com.xlr8code.server.user.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.enums.Theme;
import jakarta.validation.constraints.NotNull;

public record UpdateUserPreferencesDTO(
        @NotNull
        Language language,
        @NotNull
        Theme theme
) {
}
