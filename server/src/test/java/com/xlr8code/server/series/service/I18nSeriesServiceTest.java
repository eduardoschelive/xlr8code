package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.repository.I18nSeriesRepository;
import com.xlr8code.server.series.repository.SeriesRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class I18nSeriesServiceTest {

    @Autowired
    private SeriesSlugValidator seriesSlugValidator;

    @Autowired
    private I18nSeriesRepository i18nSeriesRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    private Series series;

    @BeforeEach
    void setUp() {
        this.series = seriesRepository.save(new Series());
    }

    @AfterEach
    void tearDown() {
        seriesRepository.deleteAll();
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
            var createI18nSeriesDTO = new SeriesLanguageDTO("title", "slug", "description");

            var createCollection = Set.of(createI18nSeriesDTO);
            var slugs = createCollection.stream().map(SeriesLanguageDTO::slug).toList();

            assertThrows(SlugAlreadyExistsException.class, () -> seriesSlugValidator.validateSlugInList(slugs));
        }

        @Test
        void it_should_validate_slug_in_list_with_owner() {
            var createI18nSeriesDTO = new SeriesLanguageDTO("title", "slug", "description");
            var randomSeries = seriesRepository.save(new Series());

            Collection<SeriesLanguageDTO> createCollection = Set.of(createI18nSeriesDTO);
            var slugs = createCollection.stream().map(SeriesLanguageDTO::slug).toList();

            assertThrows(SlugAlreadyExistsException.class, () -> seriesSlugValidator.validateSlugInList(slugs, randomSeries));
        }

        @Test
        void it_should_validate_duplicate_slugs_in_same_collection() {
            var createI18nSeriesDTO = new SeriesLanguageDTO("title", "slug", "description");
            var createI18nSeriesDTO2 = new SeriesLanguageDTO("title2", "slug", "description");

            var createCollection = Set.of(createI18nSeriesDTO, createI18nSeriesDTO2);
            var slugs = createCollection.stream().map(SeriesLanguageDTO::slug).toList();

            assertThrows(DuplicateSlugInLanguagesException.class, () -> seriesSlugValidator.validateSlugInList(slugs));
        }

    }

}