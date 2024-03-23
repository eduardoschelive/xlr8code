package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.exception.SeriesNotFoundException;
import com.xlr8code.server.series.repository.SeriesRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SeriesServiceTest {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesRepository seriesRepository;



    @Nested
    class CreateTests {

        @AfterEach
        void tearDown() {
            seriesRepository.deleteAll();
        }

        @Test
        void it_should_create_series() {
            var createSeriesDTO = buildCreateSeriesDTO();

            var createdSeries = seriesService.create(createSeriesDTO);

            assertNotNull(createdSeries);
        }

    }

    @Nested
    class FindTests {

        @BeforeEach
        void setUp() {
            seriesService.create(buildCreateSeriesDTO());
        }

        @AfterEach
        void tearDown() {
            seriesRepository.deleteAll();
        }

        @Test
        void it_should_find_all_series() {
            var result = seriesService.findAll(Set.of(Language.AMERICAN_ENGLISH), Pageable.unpaged());

            assertNotNull(result);
        }

        @Test
        void it_should_find_all_series_with_brazilian_portuguese() {
            var result = seriesService.findAll(Set.of(Language.BRAZILIAN_PORTUGUESE), Pageable.unpaged());

            var hasBrazilianPortuguese = result.stream()
                    .allMatch(series -> series.languages().containsKey(Language.BRAZILIAN_PORTUGUESE));

            assertTrue(hasBrazilianPortuguese);
        }

        @Test
        void it_should_find_series_by_id() {
            var series = seriesRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            var result = seriesService.findById(series.getId().toString(), Set.of(Language.AMERICAN_ENGLISH));

            assertNotNull(result);
        }

        @Test
        void it_should_throw_exception_when_finding_non_existing_series() {
            var languageSet = Set.of(Language.AMERICAN_ENGLISH);
            assertThrows(SeriesNotFoundException.class, () -> seriesService.findById("non-existing-id", languageSet));
        }

        @Test
        void it_should_find_series_by_search() {
            var result = seriesService.search("title", Set.of(Language.AMERICAN_ENGLISH), Pageable.ofSize(5));

            assertNotNull(result);
        }

        @Test
        void it_should_find_series_by_search_with_specific_language() {
            var result = seriesService.search("titulo", Set.of(Language.BRAZILIAN_PORTUGUESE), Pageable.ofSize(5));

            var hasBrazilianPortuguese = result.stream()
                    .allMatch(series -> series.languages().containsKey(Language.BRAZILIAN_PORTUGUESE));

            assertTrue(hasBrazilianPortuguese);
        }

        @Test
        void it_should_check_if_series_exists() {
            var series = seriesRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            var result = seriesService.existsById(series.getId().toString());

            assertTrue(result);
        }

    }

    @Nested
    class DeleteTests {

        @BeforeEach
        void setUp() {
            seriesService.create(buildCreateSeriesDTO());
        }

        @AfterEach
        void tearDown() {
            seriesRepository.deleteAll();
        }

        @Test
        void it_should_delete_series() {
            var series = seriesRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            seriesService.delete(series.getId().toString());
            var result = seriesRepository.findById(series.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void it_should_delete_series_with_string_id() {
            var series = seriesRepository.findAll(Pageable.unpaged()).getContent().getFirst();

            seriesService.delete(series.getId().toString());
            var result = seriesRepository.findById(series.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void it_should_throw_exception_when_deleting_non_existing_series() {
            assertThrows(SeriesNotFoundException.class, () -> seriesService.delete("non-existing-id"));
        }

    }

    @Nested
    class UpdateTests {

        private Series createSeries;

        @BeforeEach
        void setUp() {
            createSeries = seriesService.create(buildCreateSeriesDTO());
        }

        @AfterEach
        void tearDown() {
            seriesRepository.deleteAll();
        }

        @Test
        void it_should_update_series() {
            var series = seriesRepository.findAll(Pageable.unpaged()).getContent().getFirst();
            var updateSeriesDTO = buildCreateSeriesDTO();

            var result = seriesService.update(series.getId().toString(), updateSeriesDTO);

            assertNotNull(result);
        }

        @Test
        void it_should_throw_exception_when_updating_non_existing_series() {
            var updateDTO = buildCreateSeriesDTO();
            assertThrows(SeriesNotFoundException.class, () -> seriesService.update("non-existing-id", updateDTO));
        }

        @Test
        void it_should_allow_repeat_slug_if_series_is_owner() {
            var updateSeriesDTO = buildCreateSeriesDTO();

            var result = seriesService.update(createSeries.getId().toString(), updateSeriesDTO);

            assertNotNull(result);
        }

    }

    private SeriesDTO buildCreateSeriesDTO() {
        Map<Language, SeriesLanguageDTO> languages = Map.of(
                Language.AMERICAN_ENGLISH, new SeriesLanguageDTO("title", "description", "slug"),
                Language.BRAZILIAN_PORTUGUESE, new SeriesLanguageDTO("titulo", "descrição", "slug")
        );

        return new SeriesDTO(languages);
    }

}