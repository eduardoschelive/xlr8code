package com.xlr8code.server.series.service;

import com.xlr8code.server.series.dto.CreateSeriesDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
