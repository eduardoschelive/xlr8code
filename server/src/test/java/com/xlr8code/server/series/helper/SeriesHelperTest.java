package com.xlr8code.server.series.helper;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class SeriesHelperTest {

    @Autowired
    private SeriesServiceHelper seriesHelper;

    @Test
    void it_should_map_series_to_TranslatedDTO() {
        var series = buildSeries();

        var result = seriesHelper.mapSeriesToTranslatedSeriesDTO(Set.of(Language.BRAZILIAN_PORTUGUESE, Language.AMERICAN_ENGLISH), series);

        assertNotNull(result);
    }

    private List<Series> buildSeries() {
        return List.of(
                buildSerie(),
                buildSerie()
        );
    }

    private Series buildSerie() {
        var internationalization = Set.of(
                I18nSeries.builder()
                        .language(Language.BRAZILIAN_PORTUGUESE)
                        .title("titulo")
                        .slug("slug")
                        .description("descrição")
                        .build()
                ,
                I18nSeries.builder()
                        .language(Language.AMERICAN_ENGLISH)
                        .title("Title")
                        .slug("slug")
                        .description("description")
                        .build()
        );

        return Series.builder()
                .id(UUID.randomUUID())
                .internationalization(internationalization)
                .build();
    }

}