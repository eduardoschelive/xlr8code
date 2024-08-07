package com.xlr8code.server.category.service;

import com.xlr8code.server.category.dto.CategoryLanguageDTO;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.entity.I18nCategory;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.category.repository.I18nCategoryRepository;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
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
    private CategorySlugValidator categorySlugValidator;

    @Autowired
    private I18nCategoryRepository i18nCategoryRepository;

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

    private I18nCategory buildCreateI18nCategory(
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
            var i18nCategory = buildCreateI18nCategory("slug", "title", "description", Language.AMERICAN_ENGLISH);
            i18nCategoryRepository.save(i18nCategory);
        }

        @AfterEach
        void tearDown() {
            i18nCategoryRepository.deleteAll();
        }

        @Test
        void it_should_validate_slug_in_list() {
            var categoryLanguageDTO = new CategoryLanguageDTO("title", "slug", "description");

            var createCollection = Set.of(categoryLanguageDTO);
            var slugs = createCollection.stream().map(CategoryLanguageDTO::slug).toList();

            assertThrows(SlugAlreadyExistsException.class, () -> categorySlugValidator.validateSlugInList(slugs));
        }

        @Test
        void it_should_validate_slug_in_list_with_owner() {
            var categoryLanguageDTO = new CategoryLanguageDTO("title", "slug", "description");
            var randomCategory = categoryRepository.save(new Category());

            Collection<CategoryLanguageDTO> createCollection = Set.of(categoryLanguageDTO);
            var slugs = createCollection.stream().map(CategoryLanguageDTO::slug).toList();

            assertThrows(SlugAlreadyExistsException.class, () -> categorySlugValidator.validateSlugInList(slugs, randomCategory));
        }

        @Test
        void it_should_validate_duplicate_slugs_in_same_collection() {
            var categoryLanguageDTO = new CategoryLanguageDTO("title", "slug", "description");
            var categoryLanguageDTO2 = new CategoryLanguageDTO("title2", "slug", "description");

            var createCollection = Set.of(categoryLanguageDTO, categoryLanguageDTO2);
            var slugs = createCollection.stream().map(CategoryLanguageDTO::slug).toList();

            assertThrows(DuplicateSlugInLanguagesException.class, () -> categorySlugValidator.validateSlugInList(slugs));
        }

    }

}