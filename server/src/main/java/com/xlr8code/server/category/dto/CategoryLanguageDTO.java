package com.xlr8code.server.category.dto;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.entity.I18nCategory;
import com.xlr8code.server.common.enums.Language;
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
        var i18nCategory = findI18nCategory(category, language).orElseGet(I18nCategory::new);

        i18nCategory.setCategory(category);
        i18nCategory.setLanguage(language);
        i18nCategory.setTitle(this.title());
        i18nCategory.setSlug(this.slug());
        i18nCategory.setDescription(this.description());

        return i18nCategory;
    }

    private Optional<I18nCategory> findI18nCategory(Category category, Language language) {
        return Optional.ofNullable(category.getI18nCategories())
                .flatMap(i18nCategories -> i18nCategories.stream()
                        .filter(i18n -> i18n.getLanguage().equals(language))
                        .findFirst());
    }

}
