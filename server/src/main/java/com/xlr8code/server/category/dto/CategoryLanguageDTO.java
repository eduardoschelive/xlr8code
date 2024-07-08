package com.xlr8code.server.category.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.category.entity.I18nCategory;
import com.xlr8code.server.category.entity.Category;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record CategoryLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String description
) {

    public I18nCategory toEntity(Category category, Language language) {
        var i18nSeries = findI18nSeries(category, language).orElseGet(I18nCategory::new);

        i18nSeries.setCategory(category);
        i18nSeries.setLanguage(language);
        i18nSeries.setTitle(this.title());
        i18nSeries.setSlug(this.slug());
        i18nSeries.setDescription(this.description());

        return i18nSeries;
    }

    private Optional<I18nCategory> findI18nSeries(Category category, Language language) {
        return Optional.ofNullable(category.getI18nCategories())
                .flatMap(i18nSeries -> i18nSeries.stream()
                        .filter(i18n -> i18n.getLanguage().equals(language))
                        .findFirst());
    }

}
