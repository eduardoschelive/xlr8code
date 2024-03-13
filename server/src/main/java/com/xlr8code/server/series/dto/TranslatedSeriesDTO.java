package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.enums.Language;

import java.util.Map;
import java.util.UUID;

public record TranslatedSeriesDTO(
        UUID id,
        Map<Language, SeriesTranslationDTO> languages
) {

    public static TranslatedSeriesDTO fromEntity(UUID id, Map<Language, SeriesTranslationDTO> languages) {
        return new TranslatedSeriesDTO(id, languages);
    }

}
