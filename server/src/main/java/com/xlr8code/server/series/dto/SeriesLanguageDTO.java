package com.xlr8code.server.series.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record SeriesLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String description
) {

    public I18nSeries toEntity(Series series, Language language) {
        var i18nSeries = findI18nSeries(series, language).orElseGet(I18nSeries::new);

        i18nSeries.setSeries(series);
        i18nSeries.setLanguage(language);
        i18nSeries.setTitle(this.title());
        i18nSeries.setSlug(this.slug());
        i18nSeries.setDescription(this.description());

        return i18nSeries;
    }

    private Optional<I18nSeries> findI18nSeries(Series series, Language language) {
        return Optional.ofNullable(series.getI18nSeries())
                .flatMap(i18nSeries -> i18nSeries.stream()
                        .filter(i18n -> i18n.getLanguage().equals(language))
                        .findFirst());
    }

}
