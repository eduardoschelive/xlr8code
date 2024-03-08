package com.xlr8code.server.series.service;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguagesDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final I18nSeriesService i18nSeriesService;

    /**
     * @param createSeriesDTO the series to be created
     * @return the created series
     */
    @Transactional
    public Series create(CreateSeriesDTO createSeriesDTO) {
        var series = new Series();
        var newSeries = seriesRepository.save(series);

        var languagesSeries = createSeriesDTO.languages().entrySet().stream()
                .map(entry -> entry.getValue().toEntity(newSeries, entry.getKey()))
                .toList();

        i18nSeriesService.create(languagesSeries);

        return series;
    }

    @Transactional
    public Page<SeriesLanguagesDTO> findAll(Set<Language> languages, Pageable pageable) {
        Page<Series> seriesPage = seriesRepository.findAll(pageable);
        List<SeriesLanguagesDTO> seriesLanguagesDTOList = this.getSeriesDTOLanguages(languages, seriesPage.getContent());

        return new PageImpl<>(seriesLanguagesDTOList, pageable, seriesPage.getTotalElements());
    }

    private List<SeriesLanguagesDTO>  getSeriesDTOLanguages(Set<Language> languages, List<Series> series) {
        var seriesLanguagesDTOList = new ArrayList<SeriesLanguagesDTO>();

        series.forEach(currentSeries -> {
            var  seriesDTOLanguages = this.getSeriesDTOLanguages(languages, currentSeries);
            if (!seriesDTOLanguages.languages().isEmpty()) {
                seriesLanguagesDTOList.add(seriesDTOLanguages);
            }
        });

        return seriesLanguagesDTOList;
    }

    private SeriesLanguagesDTO getSeriesDTOLanguages(Set<Language> languages, Series currentSeries) {
        Map<Language, SeriesDTO> seriesLanguages = new EnumMap<>(Language.class);

        for (var i18nSeries : currentSeries.getI18nSeries()) {
            var language = i18nSeries.getLanguage();
            if (languages.contains(language)) {
                seriesLanguages.put(language, SeriesDTO.fromEntity(i18nSeries));
            }
        }

        return SeriesLanguagesDTO.fromEntity(currentSeries.getId(), seriesLanguages);
    }

}
