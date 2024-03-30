package com.xlr8code.server.utils;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;

import java.util.Map;

public class SeriesTestUtils {

    public static SeriesDTO buildSeriesDTO() {
        Map<Language, SeriesLanguageDTO> languages = Map.of(
                Language.AMERICAN_ENGLISH, new SeriesLanguageDTO("title", "slug1", "description"),
                Language.BRAZILIAN_PORTUGUESE, new SeriesLanguageDTO("titulo", "slug2", "descrição")
        );

        return new SeriesDTO(languages);
    }

}
