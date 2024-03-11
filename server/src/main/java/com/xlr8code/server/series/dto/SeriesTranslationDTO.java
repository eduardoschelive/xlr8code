package com.xlr8code.server.series.dto;

import com.xlr8code.server.series.entity.I18nSeries;

public record SeriesTranslationDTO(
        String title,
        String slug,
        String description
) {

    public static SeriesTranslationDTO fromEntity(I18nSeries i18nSeries) {
        return new SeriesTranslationDTO(i18nSeries.getTitle(), i18nSeries.getSlug(), i18nSeries.getDescription());
    }

}
