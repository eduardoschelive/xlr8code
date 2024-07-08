package com.xlr8code.server.category.dto;

import com.xlr8code.server.category.entity.I18nCategory;

public record CategoryTranslationDTO(
        String title,
        String slug,
        String description
) {

    public static CategoryTranslationDTO fromEntity(I18nCategory i18NCategory) {
        return new CategoryTranslationDTO(i18NCategory.getTitle(), i18NCategory.getSlug(), i18NCategory.getDescription());
    }

}
