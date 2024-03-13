package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.enums.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record CreateSeriesDTO(
        @NotEmpty
        Map<Language, @Valid CreateSeriesLanguageDTO> languages
) {
}
