package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.dto.CreateSeriesLanguageDTO;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SeriesServiceTest {

    @Autowired
    private SeriesService seriesService;

    @Nested
    class CreateTests {
        @Test
        void it_should_create_series() {
            var createSeriesDTO = buildCreateSeriesDTO();

            var createdSeries = seriesService.create(createSeriesDTO);

            assertNotNull(createdSeries);
        }

        private CreateSeriesDTO buildCreateSeriesDTO() {
            Map<Language, CreateSeriesLanguageDTO> languages = Map.of(
                    Language.AMERICAN_ENGLISH, new CreateSeriesLanguageDTO("title", "description", "slug"),
                    Language.BRAZILIAN_PORTUGUESE, new CreateSeriesLanguageDTO("titulo", "descrição", "slug")
            );

            return new CreateSeriesDTO(languages);
        }

    }
}