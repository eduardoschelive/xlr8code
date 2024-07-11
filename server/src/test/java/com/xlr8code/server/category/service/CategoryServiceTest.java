package com.xlr8code.server.category.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.utils.CategoryTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;


    @Nested
    class CreateTests {

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_create_category() {
            var categoryDTO = CategoryTestUtils.buildCategoryDTO();

            var category = categoryService.create(categoryDTO);

            assertNotNull(category);
        }

    }

    @Nested
    class FindTests {

        @BeforeEach
        void setUp() {
            categoryService.create(CategoryTestUtils.buildCategoryDTO());
        }

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_find_all_category() {
            var result = categoryService.findAll(Map.of(), Set.of(Language.AMERICAN_ENGLISH));

            assertNotNull(result);
        }

        @Test
        void it_should_find_all_categories_with_brazilian_portuguese() {
            var result = categoryService.findAll(Map.of(), Set.of(Language.BRAZILIAN_PORTUGUESE));

            var hasBrazilianPortuguese = result.stream()
                    .allMatch(translatedCategoryDTO -> translatedCategoryDTO.languages().containsKey(Language.BRAZILIAN_PORTUGUESE));

            assertTrue(hasBrazilianPortuguese);
        }

        @Test
        void it_should_find_category_by_id() {
            var category = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            var result = categoryService.findById(category.getId().toString(), Set.of(Language.AMERICAN_ENGLISH));

            assertNotNull(result);
        }

        @Test
        void it_should_throw_exception_when_finding_non_existing_category() {
            var languageSet = Set.of(Language.AMERICAN_ENGLISH);
            assertThrows(CategoryNotFoundException.class, () -> categoryService.findById("non-existing-id", languageSet));
        }

        @Test
        void it_should_check_if_category_exists() {
            var category = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            var result = categoryService.existsById(category.getId().toString());

            assertTrue(result);
        }

    }

    @Nested
    class DeleteTests {

        @BeforeEach
        void setUp() {
            categoryService.create(CategoryTestUtils.buildCategoryDTO());
        }

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_delete_category() {
            var category = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            categoryService.delete(category.getId().toString());
            var result = categoryRepository.findById(category.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void it_should_throw_exception_when_deleting_non_existing_category() {
            assertThrows(CategoryNotFoundException.class, () -> categoryService.delete("non-existing-id"));
        }

    }

    @Nested
    class UpdateTests {

        private Category createCategory;

        @BeforeEach
        void setUp() {
            createCategory = categoryService.create(CategoryTestUtils.buildCategoryDTO());
        }

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_update_category() {
            var category = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();
            var categoryDTO = CategoryTestUtils.buildCategoryDTO();

            var result = categoryService.update(category.getId().toString(), categoryDTO);

            assertNotNull(result);
        }

        @Test
        void it_should_throw_exception_when_updating_non_existing_category() {
            var updateDTO = CategoryTestUtils.buildCategoryDTO();
            assertThrows(CategoryNotFoundException.class, () -> categoryService.update("non-existing-id", updateDTO));
        }

        @Test
        void it_should_allow_repeat_slug_if_category_is_owner() {
            var categoryDTO = CategoryTestUtils.buildCategoryDTO();

            var result = categoryService.update(createCategory.getId().toString(), categoryDTO);

            assertNotNull(result);
        }

    }

}