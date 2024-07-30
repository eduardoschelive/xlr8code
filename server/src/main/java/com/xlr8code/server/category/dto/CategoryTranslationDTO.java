package com.xlr8code.server.category.dto;

import com.xlr8code.server.category.entity.I18nCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategoryTranslation")
public record CategoryTranslationDTO(
        @Schema(description = "The title of the category.")
        String title,
        @Schema(description = "The slug of the category.")
        String slug,
        @Schema(description = "The description of the category.")
        String description
) {

    public static CategoryTranslationDTO fromEntity(I18nCategory i18NCategory) {
        return new CategoryTranslationDTO(i18NCategory.getTitle(), i18NCategory.getSlug(), i18NCategory.getDescription());
    }

}
