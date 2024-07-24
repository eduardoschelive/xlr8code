package com.xlr8code.server.category.dto;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.entity.I18nCategory;
import com.xlr8code.server.common.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(name = "TranslatedCategory")
public record TranslatedCategoryDTO(
        @Schema(description = "The unique identifier of the category.")
        UUID id,
        @Schema(description = "The date and time when the category was created.")
        Instant createdAt,
        @Schema(description = "The date and time when the category was last updated.")
        Instant updatedAt,
        @Schema(description = "The different translations of the category.")
        Map<Language, CategoryTranslationDTO> languages
) {

    public static TranslatedCategoryDTO fromEntity(Category category, Set<Language> languages) {
        var categoryLanguages = category.getI18nCategories().stream()
                .filter(i18n -> languages.contains(i18n.getLanguage()))
                .collect(Collectors.toMap(
                        I18nCategory::getLanguage,
                        CategoryTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return new TranslatedCategoryDTO(category.getId(), category.getCreatedAt(), category.getUpdatedAt(), categoryLanguages);
    }

    public static TranslatedCategoryDTO fromEntity(Category category) {
        var categoryLanguages = category.getI18nCategories().stream()
                .collect(Collectors.toMap(
                        I18nCategory::getLanguage,
                        CategoryTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return new TranslatedCategoryDTO(category.getId(), category.getCreatedAt(), category.getUpdatedAt(), categoryLanguages);
    }

}
