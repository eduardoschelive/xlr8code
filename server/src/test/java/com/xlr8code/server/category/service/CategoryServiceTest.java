package com.xlr8code.server.category.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.exception.CategoryNotFoundException;
import com.xlr8code.server.category.repository.CategoryRepository;
import com.xlr8code.server.utils.SeriesTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

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
        void it_should_create_series() {
            var createSeriesDTO = SeriesTestUtils.buildSeriesDTO();

            var createdSeries = categoryService.create(createSeriesDTO);

            assertNotNull(createdSeries);
        }

    }

    @Nested
    class FindTests {

        @BeforeEach
        void setUp() {
            categoryService.create(SeriesTestUtils.buildSeriesDTO());
        }

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_find_all_series() {
            var result = categoryService.findAll(Set.of(Language.AMERICAN_ENGLISH), Pageable.unpaged());

            assertNotNull(result);
        }

        @Test
        void it_should_find_all_series_with_brazilian_portuguese() {
            var result = categoryService.findAll(Set.of(Language.BRAZILIAN_PORTUGUESE), Pageable.unpaged());

            var hasBrazilianPortuguese = result.stream()
                    .allMatch(series -> series.languages().containsKey(Language.BRAZILIAN_PORTUGUESE));

            assertTrue(hasBrazilianPortuguese);
        }

        @Test
        void it_should_find_series_by_id() {
            var series = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            var result = categoryService.findById(series.getId().toString(), Set.of(Language.AMERICAN_ENGLISH));

            assertNotNull(result);
        }

        @Test
        void it_should_throw_exception_when_finding_non_existing_series() {
            var languageSet = Set.of(Language.AMERICAN_ENGLISH);
            assertThrows(CategoryNotFoundException.class, () -> categoryService.findById("non-existing-id", languageSet));
        }

        @Test
        void it_should_find_series_by_search() {
            var result = categoryService.search("title", Set.of(Language.AMERICAN_ENGLISH), Pageable.ofSize(5));

            assertNotNull(result);
        }

        @Test
        void it_should_find_series_by_search_with_specific_language() {
            var result = categoryService.search("titulo", Set.of(Language.BRAZILIAN_PORTUGUESE), Pageable.ofSize(5));

            var hasBrazilianPortuguese = result.stream()
                    .allMatch(series -> series.languages().containsKey(Language.BRAZILIAN_PORTUGUESE));

            assertTrue(hasBrazilianPortuguese);
        }

        @Test
        void it_should_check_if_series_exists() {
            var series = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            var result = categoryService.existsById(series.getId().toString());

            assertTrue(result);
        }

    }

    @Nested
    class DeleteTests {

        @BeforeEach
        void setUp() {
            categoryService.create(SeriesTestUtils.buildSeriesDTO());
        }

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_delete_series() {
            var series = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            categoryService.delete(series.getId().toString());
            var result = categoryRepository.findById(series.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void it_should_delete_series_with_string_id() {
            var series = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            categoryService.delete(series.getId().toString());
            var result = categoryRepository.findById(series.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void it_should_throw_exception_when_deleting_non_existing_series() {
            assertThrows(CategoryNotFoundException.class, () -> categoryService.delete("non-existing-id"));
        }

    }

    @Nested
    class UpdateTests {

        private Category createCategory;

        @BeforeEach
        void setUp() {
            createCategory = categoryService.create(SeriesTestUtils.buildSeriesDTO());
        }

        @AfterEach
        void tearDown() {
            categoryRepository.deleteAll();
        }

        @Test
        void it_should_update_series() {
            var series = categoryRepository.findAll(Pageable.unpaged()).getContent().getFirst();
            var updateSeriesDTO = SeriesTestUtils.buildSeriesDTO();

            var result = categoryService.update(series.getId().toString(), updateSeriesDTO);

            assertNotNull(result);
        }

        @Test
        void it_should_throw_exception_when_updating_non_existing_series() {
            var updateDTO = SeriesTestUtils.buildSeriesDTO();
            assertThrows(CategoryNotFoundException.class, () -> categoryService.update("non-existing-id", updateDTO));
        }

        @Test
        void it_should_allow_repeat_slug_if_series_is_owner() {
            var updateSeriesDTO = SeriesTestUtils.buildSeriesDTO();

            var result = categoryService.update(createCategory.getId().toString(), updateSeriesDTO);

            assertNotNull(result);
        }

    }

}