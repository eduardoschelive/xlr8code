package com.xlr8code.server.category.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.PropertyDoesNotExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.category.dto.CategoryDTO;
import com.xlr8code.server.category.dto.TranslatedCategoryDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.repository.CategoryRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategorySlugValidator seriesSlugValidator;

    @Resource
    @Lazy
    private CategoryService self;

    /**
     * @param categoryDTO the series to be created
     * @return the created series
     */
    @Transactional
    public Category create(CategoryDTO categoryDTO) {
        this.seriesSlugValidator.validateSlugs(categoryDTO.languages().values());
        var emptySeries = new Category();
        var series = categoryDTO.toEntity(emptySeries);
        return this.categoryRepository.save(series);
    }

    /**
     * @param languages the languages to filter
     * @param pageable  the page to be returned
     * @return the series with the specified languages
     * @throws PropertyDoesNotExistsException if specified to sort in pageable does not exist
     */
    @Transactional(readOnly = true)
    public Page<TranslatedCategoryDTO> findAll(Set<Language> languages, Pageable pageable) {
        try {
            var seriesPage = categoryRepository.findAll(pageable);
            var filteredTranslatedSeriesDTOs = this.mapAndFilterEmptyLanguages(languages, seriesPage.getContent());

            return new PageImpl<>(filteredTranslatedSeriesDTOs, pageable, seriesPage.getTotalElements());
        } catch (PropertyReferenceException e) {
            throw new PropertyDoesNotExistsException(e.getPropertyName());
        }
    }

    /**
     * @param query     the query to be searched
     * @param languages the languages to filter
     * @param pageable  the page to be returned
     * @return the series with the specified languages
     * @apiNote this ignores the sorting and return with descending order
     */
    @Transactional(readOnly = true)
    public Page<TranslatedCategoryDTO> search(String query, Set<Language> languages, Pageable pageable) {
        var seriesPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());
        var page = categoryRepository.search(query, languages, seriesPage);

        var filteredTranslatedSeriesDTOs = this.mapAndFilterEmptyLanguages(languages, page.getContent());

        return new PageImpl<>(filteredTranslatedSeriesDTOs, pageable, page.getTotalElements());
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
     * @param uuidString the series id
     * @param categoryDTO  the series to be updated
     * @return the updated translated series with the specified id and languages
     */
    @Transactional
    public TranslatedCategoryDTO update(String uuidString, CategoryDTO categoryDTO) {
        var existingSeries = self.findById(uuidString);
        this.seriesSlugValidator.validateSlugs(categoryDTO, existingSeries);

        var updatedSeries = categoryDTO.toEntity(existingSeries);
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

    /**
     * @param languages  the languages to filter
     * @param categoryList the series to be mapped
     * @return the series languages with non-empty languages
     */
    private List<TranslatedCategoryDTO> mapAndFilterEmptyLanguages(Set<Language> languages, List<Category> categoryList) {
        var seriesLanguagesDTOList = this.mapSeriesToTranslatedSeriesDTO(languages, categoryList);
        return this.filterEmptyLanguages(seriesLanguagesDTOList);
    }

    /**
     * @param languages  the languages to be filtered
     * @param categoryList the series to be mapped
     * @return the series languages with non-empty languages
     */
    private List<TranslatedCategoryDTO> mapSeriesToTranslatedSeriesDTO(Set<Language> languages, List<Category> categoryList) {
        return categoryList.stream()
                .map(s -> TranslatedCategoryDTO.fromEntity(s, languages))
                .toList();
    }

    /**
     * @param seriesLanguagesDTOList the series languages to be filtered
     * @return the series languages with non-empty languages
     */
    private List<TranslatedCategoryDTO> filterEmptyLanguages(List<TranslatedCategoryDTO> seriesLanguagesDTOList) {
        return seriesLanguagesDTOList.stream()
                .filter(dto -> !dto.languages().isEmpty())
                .toList();
    }

}
