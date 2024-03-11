package com.xlr8code.server.series.helper;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.series.dto.SeriesTranslationDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeriesHelper {

    /**
     * @param languages the languages to filter
     * @param series the series to be filtered
     * @return the series with the specified languages
     */
    public List<TranslatedSeriesDTO> buildSeriesLanguagesDTO(Set<Language> languages, List<Series> series) {
        return series.stream()
                .map(s -> buildSeriesLanguagesDTO(languages, s))
                .filter(dto -> !dto.languages().isEmpty())
                .toList();
    }

    /**
     * @param languages the languages to filter
     * @param currentSeries the series to be filtered
     * @return the series with the specified languages
     */
    private TranslatedSeriesDTO buildSeriesLanguagesDTO(Set<Language> languages, Series currentSeries) {
        Map<Language, SeriesTranslationDTO> seriesLanguages = currentSeries.getInternationalization().stream()
                .filter(i18n -> languages.contains(i18n.getLanguage()))
                .collect(Collectors.toMap(
                        I18nSeries::getLanguage,
                        SeriesTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return TranslatedSeriesDTO.fromEntity(currentSeries.getId(), seriesLanguages);
    }

}
