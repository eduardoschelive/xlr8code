package com.xlr8code.server.category.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.category.dto.CategoryLanguageDTO;
import com.xlr8code.server.category.entity.I18nCategory;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.repository.I18nCategoryRepository;
import com.xlr8code.server.category.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class I18NCategoryServiceTest {

    @Autowired
    private CategorySlugValidator seriesSlugValidator;

    @Autowired
    private I18nCategoryRepository i18nSeriesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        this.category = categoryRepository.save(new Category());
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    private I18nCategory buildCreateI18nSeries(
            String slug,
            String title,
            String description,
            Language language
    ) {
        return I18nCategory.builder()
                .slug(slug)
                .title(title)
                .language(language)
                .description(description)
                .category(this.category)
                .build();
    }

    @Nested
    class SlugValidationTests {

        @BeforeEach
        void setUp() {
            var i18nSeries = buildCreateI18nSeries("slug", "title", "description", Language.AMERICAN_ENGLISH);
            i18nSeriesRepository.save(i18nSeries);
        }

        @AfterEach
        void tearDown() {
            i18nSeriesRepository.deleteAll();
        }

        @Test
        void it_should_validate_slug_in_list() {
            var createI18nSeriesDTO = new CategoryLanguageDTO("title", "slug", "description");

            var createCollection = Set.of(createI18nSeriesDTO);
            var slugs = createCollection.stream().map(CategoryLanguageDTO::slug).toList();

            assertThrows(SlugAlreadyExistsException.class, () -> seriesSlugValidator.validateSlugInList(slugs));
        }

        @Test
        void it_should_validate_slug_in_list_with_owner() {
            var createI18nSeriesDTO = new CategoryLanguageDTO("title", "slug", "description");
            var randomSeries = categoryRepository.save(new Category());

            Collection<CategoryLanguageDTO> createCollection = Set.of(createI18nSeriesDTO);
            var slugs = createCollection.stream().map(CategoryLanguageDTO::slug).toList();

            assertThrows(SlugAlreadyExistsException.class, () -> seriesSlugValidator.validateSlugInList(slugs, randomSeries));
        }

        @Test
        void it_should_validate_duplicate_slugs_in_same_collection() {
            var createI18nSeriesDTO = new CategoryLanguageDTO("title", "slug", "description");
            var createI18nSeriesDTO2 = new CategoryLanguageDTO("title2", "slug", "description");

            var createCollection = Set.of(createI18nSeriesDTO, createI18nSeriesDTO2);
            var slugs = createCollection.stream().map(CategoryLanguageDTO::slug).toList();

            assertThrows(DuplicateSlugInLanguagesException.class, () -> seriesSlugValidator.validateSlugInList(slugs));
        }

    }

}