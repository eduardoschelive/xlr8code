package com.xlr8code.server.category.service;

import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.CategoryLanguageDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.repository.I18nCategoryRepository;
import com.xlr8code.server.common.validation.SlugValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategorySlugValidator implements SlugValidator<Category> {

    private final I18nCategoryRepository i18nCategoryRepository;


    @Override
    public boolean existsBySlug(String slug) {
        return i18nCategoryRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsBySlugAndOwnerNot(String slug, Category ownerEntity) {
        return i18nCategoryRepository.existsBySlugAndCategoryNot(slug, ownerEntity);
    }

    public void validateSlugs(Collection<CategoryLanguageDTO> categoryLanguageDTOs) {
        var slugs = categoryLanguageDTOs.stream().map(CategoryLanguageDTO::slug).toList();
        this.validateSlugInList(slugs);
    }

    public void validateSlugs(CategoryDTO categoryDTO, Category category) {
        var categoryLanguagesDTOs = categoryDTO.languages().values();
        var slugs = categoryLanguagesDTOs.stream().map(CategoryLanguageDTO::slug).toList();
        this.validateSlugInList(slugs, category);
    }

}
