package com.xlr8code.server.series.dto;

import jakarta.validation.constraints.NotBlank;

public record SeriesLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String description
) {
}
