package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TranslatedSeriesDTO(
        UUID id,
        Map<Language, SeriesTranslationDTO> languages
) {

    public static TranslatedSeriesDTO fromEntity(Series series, Set<Language> languages) {
        var seriesLanguages = series.getI18nSeries().stream()
                .filter(i18n -> languages.contains(i18n.getLanguage()))
                .collect(Collectors.toMap(
                        I18nSeries::getLanguage,
                        SeriesTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return new TranslatedSeriesDTO(series.getId(), seriesLanguages);
    }

    public static TranslatedSeriesDTO fromEntity(Series series) {
        var seriesLanguages = series.getI18nSeries().stream()
                .collect(Collectors.toMap(
                        I18nSeries::getLanguage,
                        SeriesTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return new TranslatedSeriesDTO(series.getId(), seriesLanguages);
    }

}
