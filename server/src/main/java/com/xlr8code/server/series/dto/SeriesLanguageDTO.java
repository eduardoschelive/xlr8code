package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import jakarta.validation.constraints.NotBlank;

public record SeriesLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String description
) {

        public I18nSeries toEntity(Series series, Language language) {
                return I18nSeries.builder()
                        .title(this.title())
                        .slug(this.slug())
                        .language(language)
                        .description(this.description())
                        .series(series)
                        .build();
        }

}
