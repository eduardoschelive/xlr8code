package com.xlr8code.server.category.dto;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.stream.Collectors;

@Schema(name = "Category")
public record CategoryDTO(
        @Schema(description = "The different translations of the category.")
        @NotEmpty
        Map<Language, @Valid CategoryLanguageDTO> languages
) {

    public Category toEntity(Category category) {
        var categoryLanguage = this.languages().entrySet().stream()
                .map(entry -> entry.getValue().toEntity(category, entry.getKey()))
                .collect(Collectors.toSet());

        category.setI18nCategories(categoryLanguage);

        return category;
    }

}
