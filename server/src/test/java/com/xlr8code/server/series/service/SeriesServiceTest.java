package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.dto.CreateSeriesLanguageDTO;
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

    private CreateSeriesDTO buildCreateSeriesDTO() {
        Map<Language, CreateSeriesLanguageDTO> languages = Map.of(
                Language.AMERICAN_ENGLISH, new CreateSeriesLanguageDTO("title", "description", "slug"),
                Language.BRAZILIAN_PORTUGUESE, new CreateSeriesLanguageDTO("titulo", "descrição", "slug")
        );

        return new CreateSeriesDTO(languages);
    }

}