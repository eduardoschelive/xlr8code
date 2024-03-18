package com.xlr8code.server.series.helper;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import com.xlr8code.server.series.dto.SeriesTranslationDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeriesServiceHelper {

    /**
     * @param languages the languages to filter
     * @param series the series to be filtered
     * @return the series with the specified languages
     */
    public List<TranslatedSeriesDTO> mapSeriesToTranslatedSeriesDTO(Set<Language> languages, List<Series> series) {
        return series.stream()
                .map(s -> mapSeriesToTranslatedSeriesDTO(languages, s))
                .filter(dto -> !dto.languages().isEmpty())
                .toList();
    }

    public TranslatedSeriesDTO mapSeriesToTranslatedSeriesDTO(Set<Language> languages, Series series) {
        return buildTranslatedSeriesDTO(languages, series);
    }

    /**
     * @param languages the languages to filter
     * @param currentSeries the series to be filtered
     * @return the series with the specified languages
     */
    private TranslatedSeriesDTO buildTranslatedSeriesDTO(Set<Language> languages, Series currentSeries) {
        var seriesLanguages = currentSeries.getInternationalization().stream()
                .filter(i18n -> languages.contains(i18n.getLanguage()))
                .collect(Collectors.toMap(
                        I18nSeries::getLanguage,
                        SeriesTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        return TranslatedSeriesDTO.fromEntity(currentSeries.getId(), seriesLanguages);
    }

    /**
     * @param seriesDTO the series to be mapped
     * @return the series mapped to {@link Series}
     */
    public Series mapSeriesDTOToEntity(SeriesDTO seriesDTO) {
        var series = new Series();
        var seriesLanguages =  seriesDTO.languages().entrySet().stream()
                .map(entry -> mapSeriesLanguageDTOToEntity(series, entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());

        series.setInternationalization(seriesLanguages);
        return series;
    }

    /**
     * @param series the series to be mapped
     * @param language the language to be mapped
     * @param createSeriesLanguageDTO the series language to be mapped
     * @return the series language mapped to {@link I18nSeries}
     */
    public I18nSeries mapSeriesLanguageDTOToEntity(Series series, Language language, SeriesLanguageDTO createSeriesLanguageDTO) {
        return I18nSeries.builder()
                .series(series)
                .language(language)
                .title(createSeriesLanguageDTO.title())
                .slug(createSeriesLanguageDTO.slug())
                .description(createSeriesLanguageDTO.description())
                .build();
    }


    /**
     * @param series          the series to be updated
     * @param updateSeriesDTO the series to be updated
     * @param languages       the languages to be updated
     * @return the series languages updated to {@link I18nSeries}
     */
    public Set<I18nSeries> updateI18nSeriesForSeries(Series series, SeriesDTO updateSeriesDTO, Set<Language> languages) {
        var updatedI18nSeries = new HashSet<I18nSeries>();

        for (var language : languages) {
            var seriesLanguageDTO = updateSeriesDTO.languages().get(language);

            var i18n = findOrCreateI18NSeriesEntity(series, language);
            updateI18NSeriesEntity(i18n, seriesLanguageDTO);

            updatedI18nSeries.add(i18n);
        }

        return updatedI18nSeries;
    }

    /**
     * @param series the series to be updated
     * @param language the language to be updated
     * @return the series language to be updated
     */
    private I18nSeries findOrCreateI18NSeriesEntity(Series series, Language language) {
        return series.getInternationalization().stream()
                .filter(i -> i.getLanguage().equals(language))
                .findFirst()
                .orElseGet(() -> I18nSeries.builder().series(series).language(language).build());
    }

    /**
     * @param i18n the series language to be updated
     * @param seriesLanguageDTO the series language to be updated
     */
    private void updateI18NSeriesEntity(I18nSeries i18n, SeriesLanguageDTO seriesLanguageDTO) {
        i18n.setTitle(seriesLanguageDTO.title());
        i18n.setDescription(seriesLanguageDTO.description());
        i18n.setSlug(seriesLanguageDTO.slug());
    }

}
