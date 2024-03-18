package com.xlr8code.server.series.service;

import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.LanguageAlreadyExistsForResourceException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.enums.Language;
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