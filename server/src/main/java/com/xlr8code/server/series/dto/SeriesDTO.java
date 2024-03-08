package com.xlr8code.server.series.dto;

import com.xlr8code.server.series.entity.I18nSeries;

public record SeriesDTO(
        String title,
        String slug,
        String description
) {

    public static SeriesDTO fromEntity(I18nSeries i18nSeries) {
        return new SeriesDTO(i18nSeries.getTitle(), i18nSeries.getSlug(), i18nSeries.getDescription());
    }

}
