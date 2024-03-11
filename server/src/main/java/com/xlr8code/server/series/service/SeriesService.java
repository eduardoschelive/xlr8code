package com.xlr8code.server.series.service;

import com.xlr8code.server.common.exception.PropertyDoesNotExistsException;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguagesDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.helper.SeriesHelper;
import com.xlr8code.server.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final I18nSeriesService i18nSeriesService;
    private final SeriesHelper seriesHelper;

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

    /**
     * @param languages the languages to filter
     * @param pageable the page to be returned
     * @return the series with the specified languages
     */
    @Transactional
    public Page<SeriesLanguagesDTO> findAll(Set<Language> languages, Pageable pageable) {
        try {
            Page<Series> seriesPage = seriesRepository.findAll(pageable);
            List<SeriesLanguagesDTO> seriesLanguagesDTOList = this.seriesHelper.buildSeriesLanguagesDTO(languages, seriesPage.getContent());
            return new PageImpl<>(seriesLanguagesDTOList, pageable, seriesPage.getTotalElements());
        } catch (PropertyReferenceException e) {
            throw new PropertyDoesNotExistsException(e.getPropertyName());
        }
    }

}
