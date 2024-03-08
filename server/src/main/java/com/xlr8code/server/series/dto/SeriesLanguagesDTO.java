package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.utils.Language;

import java.util.Map;
import java.util.UUID;

public record SeriesLanguagesDTO(
        UUID id,
        Map<Language, SeriesDTO> languages
) {

    public static SeriesLanguagesDTO fromEntity(UUID id, Map<Language, SeriesDTO> languages) {
        return new SeriesLanguagesDTO(id, languages);
    }

}
