package com.xlr8code.server.category.dto;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.category.entity.I18nCategory;
import com.xlr8code.server.category.entity.Category;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TranslatedCategoryDTO(
        UUID id,
        Map<Language, CategoryTranslationDTO> languages
) {

    public static TranslatedCategoryDTO fromEntity(Category category, Set<Language> languages) {
        var seriesLanguages = category.getI18nCategories().stream()
                .filter(i18n -> languages.contains(i18n.getLanguage()))
                .collect(Collectors.toMap(
                        I18nCategory::getLanguage,
                        CategoryTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return new TranslatedCategoryDTO(category.getId(), seriesLanguages);
    }

    public static TranslatedCategoryDTO fromEntity(Category category) {
        var seriesLanguages = category.getI18nCategories().stream()
                .collect(Collectors.toMap(
                        I18nCategory::getLanguage,
                        CategoryTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return new TranslatedCategoryDTO(category.getId(), seriesLanguages);
    }

}
