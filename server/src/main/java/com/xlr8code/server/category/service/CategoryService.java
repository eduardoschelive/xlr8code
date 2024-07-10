package com.xlr8code.server.category.service;

import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.PropertyDoesNotExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategorySlugValidator categorySlugValidator;

    @Resource
    @Lazy
    private CategoryService self;

    /**
     * @param categoryDTO the series to be created
     * @return the created series
     */
    @Transactional
    public Category create(CategoryDTO categoryDTO) {
        this.categorySlugValidator.validateSlugs(categoryDTO.languages().values());
        var emptyCategory = new Category();
        var category = categoryDTO.toEntity(emptyCategory);
        return this.categoryRepository.save(category);
    }

    /**
     * @param languages the languages to filter
     * @return the series with the specified languages
     * @throws PropertyDoesNotExistsException if specified to sort in pageable does not exist
     */
    @Transactional(readOnly = true)
    public Page<TranslatedCategoryDTO> findAll(Map<String, String> requestParams, Set<Language> languages) {
        var page = this.categoryRepository.findAll(requestParams, Category.class);
        return page.map(category -> TranslatedCategoryDTO.fromEntity(category, languages));
    }

    /**
     * @param uuid the series id
     * @return the series with the specified id
     * @throws CategoryNotFoundException if the series does not exist
     */
    @Transactional
    public Category findById(UUID uuid) {
        return categoryRepository.findById(uuid)
                .orElseThrow(() -> new CategoryNotFoundException(uuid.toString()));
    }

    /**
     * @param uuidString the series id
     * @return the series with the specified id
     * @throws CategoryNotFoundException if the series does not exist
     */
    @Transactional
    public Category findById(String uuidString) {
        var uuid = UUIDUtils.convertFromString(uuidString)
                .orElseThrow(() -> new CategoryNotFoundException(uuidString));

        return self.findById(uuid);
    }

    /**
     * @param uuidString the series id
     * @param languages  the languages to filter
     * @return the series with the specified id and languages
     */
    @Transactional
    public TranslatedCategoryDTO findById(String uuidString, Set<Language> languages) {
        var entity = self.findById(uuidString);
        return TranslatedCategoryDTO.fromEntity(entity, languages);
    }

    /**
     * @param uuidString the series id
     * @throws CategoryNotFoundException if the series does not exist
     */
    @Transactional
    public void delete(String uuidString) {
        var entity = self.findById(uuidString);
        categoryRepository.delete(entity);
    }

    /**
     * @param uuidString  the series id
     * @param categoryDTO the series to be updated
     * @return the updated translated series with the specified id and languages
     */
    @Transactional
    public TranslatedCategoryDTO update(String uuidString, CategoryDTO categoryDTO) {
        var existingCategory = self.findById(uuidString);
        this.categorySlugValidator.validateSlugs(categoryDTO, existingCategory);

        var updatedSeries = categoryDTO.toEntity(existingCategory);
        var savedEntity = categoryRepository.save(updatedSeries);

        return TranslatedCategoryDTO.fromEntity(savedEntity);
    }

    /**
     * @param id the series id
     * @return whether the series exists
     */
    @Transactional
    public boolean existsById(String id) {
        var uuid = UUIDUtils.convertFromString(id);
        return uuid.filter(this.categoryRepository::existsById).isPresent();
    }

}
