package com.xlr8code.server.category.service;

import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param categoryDTO the category to be created
     * @return the created category
     * @throws DuplicateSlugInLanguagesException if there are duplicate slugs
     * @throws SlugAlreadyExistsException        if any slug already exists
     */
    @Transactional
    public Category create(CategoryDTO categoryDTO) {
        this.categorySlugValidator.validateSlugs(categoryDTO.languages().values());
        var emptyCategory = new Category();
        var category = categoryDTO.toEntity(emptyCategory);
        return this.categoryRepository.save(category);
    }

    /**
     * @param languages     the languages to filter
     * @param specification the specification to filter
     * @param pageable      the pageable to filter
     * @return the categories with the specified languages
     */
    @Transactional(readOnly = true)
    public Page<TranslatedCategoryDTO> findAll(Specification<Category> specification, Pageable pageable, Set<Language> languages) {
        var page = this.categoryRepository.findAll(specification, pageable);
        return page.map(category -> TranslatedCategoryDTO.fromEntity(category, languages));
    }

    /**
     * @param uuid the category id
     * @return the category with the specified id
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Transactional
    public Category findById(UUID uuid) {
        return this.categoryRepository.findById(uuid)
                .orElseThrow(() -> new CategoryNotFoundException(uuid.toString()));
    }

    /**
     * @param uuidString the category id
     * @return the category with the specified id
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Transactional
    public Category findById(String uuidString) {
        var uuid = UUIDUtils.convertFromString(uuidString)
                .orElseThrow(() -> new CategoryNotFoundException(uuidString));

        return self.findById(uuid);
    }

    /**
     * @param uuidString the category id
     * @param languages  the languages to filter
     * @return the category with the specified id and languages
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Transactional
    public TranslatedCategoryDTO findById(String uuidString, Set<Language> languages) {
        var entity = self.findById(uuidString);
        return TranslatedCategoryDTO.fromEntity(entity, languages);
    }

    /**
     * @param uuidString the category id
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Transactional
    public void delete(String uuidString) {
        var entity = self.findById(uuidString);
        this.categoryRepository.delete(entity);
    }

    /**
     * @param uuidString  the category id
     * @param categoryDTO the category to be updated
     * @return the updated translated category with the specified id and languages
     * @throws CategoryNotFoundException         if the category does not exist
     * @throws DuplicateSlugInLanguagesException if there are duplicate slugs
     * @throws SlugAlreadyExistsException        if any slug already exists
     */
    @Transactional
    public TranslatedCategoryDTO update(String uuidString, CategoryDTO categoryDTO) {
        var existingCategory = self.findById(uuidString);
        this.categorySlugValidator.validateSlugs(categoryDTO, existingCategory);

        var category = categoryDTO.toEntity(existingCategory);
        var savedEntity = this.categoryRepository.save(category);

        return TranslatedCategoryDTO.fromEntity(savedEntity);
    }

    /**
     * @param id the category id
     * @return whether the category exists
     */
    @Transactional
    public boolean existsById(String id) {
        var uuid = UUIDUtils.convertFromString(id);
        return uuid.filter(this.categoryRepository::existsById).isPresent();
    }

}
