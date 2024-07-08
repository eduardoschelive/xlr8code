package com.xlr8code.server.category.dto;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.enums.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.stream.Collectors;

public record CategoryDTO(
        @NotEmpty
        Map<Language, @Valid CategoryLanguageDTO> languages
) {

    public Category toEntity(Category category) {
        var seriesLanguages = this.languages().entrySet().stream()
                .map(entry -> entry.getValue().toEntity(category, entry.getKey()))
                .collect(Collectors.toSet());

        category.setI18nCategories(seriesLanguages);

        return category;
    }

}
