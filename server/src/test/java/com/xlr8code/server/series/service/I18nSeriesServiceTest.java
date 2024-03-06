package com.xlr8code.server.series.service;

import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.LanguageAlreadyExistsForResourceException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.repository.SeriesRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class I18nSeriesServiceTest {

    @Autowired
    private I18nSeriesService i18nSeriesService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Nested
    class CreateTests {

        private Series series;

        @BeforeEach
        void setUp() {
            this.series = seriesRepository.save(new Series());
        }

        @AfterEach
        void tearDown() {
            seriesRepository.deleteAll();
        }

        @Test
        void it_should_create_i18n_series() {
            var createI18nSeries = buildCreateI18nSeries("slug", "title", "description", Language.AMERICAN_ENGLISH);

            var createdI18nSeries = i18nSeriesService.create(createI18nSeries);

            assertNotNull(createdI18nSeries);
        }

        @Test
        void it_should_not_create_i18n_series_with_same_slug() {
            var slug = "slug";
            var createI18nSeries = buildCreateI18nSeries(slug, "title", "description", Language.AMERICAN_ENGLISH);
            i18nSeriesService.create(createI18nSeries);

            var createI18nSeriesWithSameSlug = buildCreateI18nSeries(slug, "title", "description", Language.BRAZILIAN_PORTUGUESE);

            assertThrows(SlugAlreadyExistsException.class, () -> i18nSeriesService.create(createI18nSeriesWithSameSlug));
        }

        @Test
        void it_should_not_create_i18n_series_with_same_language() {
            var language = Language.AMERICAN_ENGLISH;
            var createI18nSeries = buildCreateI18nSeries("slug", "title", "description", language);
            i18nSeriesService.create(createI18nSeries);

            var createI18nSeriesWithSameLanguage = buildCreateI18nSeries("slug", "title", "description", language);

            assertThrows(LanguageAlreadyExistsForResourceException.class, () -> i18nSeriesService.create(createI18nSeriesWithSameLanguage));
        }

        @Test
        void it_should_create_i18n_series_in_list() {
            var createI18nSeries = buildCreateI18nSeries("slug", "title", "description", Language.AMERICAN_ENGLISH);
            var createI18nSeries2 = buildCreateI18nSeries("slug2", "title2", "description2", Language.BRAZILIAN_PORTUGUESE);

            List<I18nSeries> listToCreate = List.of(createI18nSeries, createI18nSeries2);

            var createdI18nSeries = i18nSeriesService.create(listToCreate);

            assertNotNull(createdI18nSeries);
        }

        @Test
        void it_should_not_create_i18n_series_in_list_with_same_slug() {
            var slug = "slug";
            var createI18nSeries = buildCreateI18nSeries(slug, "title", "description", Language.AMERICAN_ENGLISH);
            var createI18nSeries2 = buildCreateI18nSeries(slug, "title2", "description2", Language.BRAZILIAN_PORTUGUESE);

            List<I18nSeries> listToCreate = List.of(createI18nSeries, createI18nSeries2);

            assertThrows(DuplicateSlugInLanguagesException.class, () -> i18nSeriesService.create(listToCreate));
        }

        private I18nSeries buildCreateI18nSeries(
                String slug,
                String title,
                String description,
                Language language
        ) {
            return I18nSeries.builder()
                    .slug(slug)
                    .title(title)
                    .language(language)
                    .description(description)
                    .series(this.series)
                    .build();
        }

    }
}