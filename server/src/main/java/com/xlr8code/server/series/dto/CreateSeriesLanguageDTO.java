package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import jakarta.validation.constraints.NotBlank;

public record CreateSeriesLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String description
) {

    public I18nSeries toEntity(Series series, Language language) {
        return I18nSeries.builder()
                .series(series)
                .title(title)
                .slug(slug)
                .language(language)
                .description(description)
                .build();
    }

}
