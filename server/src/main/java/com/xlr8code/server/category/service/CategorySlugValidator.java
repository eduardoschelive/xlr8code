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


    /**
     * @param slug slug to check if exists
     * @return true if slug exists
     */
    @Override
    public boolean existsBySlug(String slug) {
        return i18nCategoryRepository.existsBySlug(slug);
    }

    /**
     * @param slug        slug to check if exists
     * @param ownerEntity owner to exclude from the check
     * @return true if slug exists and ownerEntity is not the owner of that slug
     */
    @Override
    public boolean existsBySlugAndOwnerNot(String slug, Category ownerEntity) {
        return i18nCategoryRepository.existsBySlugAndCategoryNot(slug, ownerEntity);
    }

    /**
     * @param categoryLanguageDTOs the category language DTOs to validate
     * @throws com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException if there are duplicate slugs
     * @throws com.xlr8code.server.common.exception.SlugAlreadyExistsException        if any slug already exists
     */
    public void validateSlugs(Collection<CategoryLanguageDTO> categoryLanguageDTOs) {
        var slugs = categoryLanguageDTOs.stream().map(CategoryLanguageDTO::slug).toList();
        this.validateSlugInList(slugs);
    }

    /**
     * @param categoryDTO the category DTO to validate
     * @param category    the category to validate
     * @throws com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException if there are duplicate slugs
     * @throws com.xlr8code.server.common.exception.SlugAlreadyExistsException        if any slug already exists
     */
    public void validateSlugs(CategoryDTO categoryDTO, Category category) {
        var categoryLanguagesDTOs = categoryDTO.languages().values();
        var slugs = categoryLanguagesDTOs.stream().map(CategoryLanguageDTO::slug).toList();
        this.validateSlugInList(slugs, category);
    }

}
