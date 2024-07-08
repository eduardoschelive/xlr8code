package com.xlr8code.server.category.service;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.validation.SlugValidator;
import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.CategoryLanguageDTO;
import com.xlr8code.server.category.repository.I18nCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategorySlugValidator implements SlugValidator<Category> {

    private final I18nCategoryRepository i18nSeriesRepository;


    @Override
    public boolean existsBySlug(String slug) {
        return i18nSeriesRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsBySlugAndOwnerNot(String slug, Category ownerEntity) {
        return i18nSeriesRepository.existsBySlugAndCategoryNot(slug, ownerEntity);
    }

    public void validateSlugs(Collection<CategoryLanguageDTO> seriesLanguageDTOs) {
        var slugs = seriesLanguageDTOs.stream().map(CategoryLanguageDTO::slug).toList();
        this.validateSlugInList(slugs);
    }

    public void validateSlugs(CategoryDTO categoryDTO, Category category) {
        var seriesLanguageDTOs = categoryDTO.languages().values();
        var slugs = seriesLanguageDTOs.stream().map(CategoryLanguageDTO::slug).toList();
        this.validateSlugInList(slugs, category);
    }

}
