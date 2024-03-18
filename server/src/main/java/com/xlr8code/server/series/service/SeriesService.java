package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.PropertyDoesNotExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.exception.SeriesNotFoundException;
import com.xlr8code.server.series.helper.SeriesServiceHelper;
import com.xlr8code.server.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final I18nSeriesService i18nSeriesService;
    private final SeriesServiceHelper seriesHelper;

    /**
     * @param seriesDTO the series to be created
     * @return the created series
     */
    @Transactional
    public Series create(SeriesDTO seriesDTO) {
        var series = this.seriesHelper.mapSeriesDTOToEntity(seriesDTO);

        this.i18nSeriesService.validateSlugInList(series.getInternationalization());

        return this.seriesRepository.save(series);
    }

    /**
     * @param languages the languages to filter
     * @param pageable  the page to be returned
     * @return the series with the specified languages
     * @throws PropertyDoesNotExistsException if specified to sort in pageable does not exist
     */
    @Transactional(readOnly = true)
    public Page<TranslatedSeriesDTO> findAll(Set<Language> languages, Pageable pageable) {
        try {
            var seriesPage = seriesRepository.findAll(pageable);
            var seriesLanguagesDTOList = this.seriesHelper.mapSeriesToTranslatedSeriesDTO(languages, seriesPage.getContent());
            return new PageImpl<>(seriesLanguagesDTOList, pageable, seriesPage.getTotalElements());
        } catch (PropertyReferenceException e) {
            throw new PropertyDoesNotExistsException(e.getPropertyName());
        }
    }

    /**
     * @param query     the query to be searched
     * @param languages the languages to filter
     * @param pageable  the page to be returned
     * @return the series with the specified languages
     * @apiNote this ignores the sorting and return with descending order
     */
    @Transactional(readOnly = true)
    public Page<TranslatedSeriesDTO> search(String query, Set<Language> languages, Pageable pageable) {
        var seriesPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        var page = seriesRepository.search(query, languages, seriesPage);
        var seriesLanguagesDTOList = this.seriesHelper.mapSeriesToTranslatedSeriesDTO(languages, page.getContent());
        return new PageImpl<>(seriesLanguagesDTOList, pageable, page.getTotalElements());
    }

    /**
     * @param uuid the series id
     * @return the series with the specified id
     * @throws SeriesNotFoundException if the series does not exist
     */
    @Transactional
    public Series findById(UUID uuid) {
        return seriesRepository.findById(uuid)
                .orElseThrow(() -> new SeriesNotFoundException(uuid.toString()));
    }

    /**
     * @param uuidString the series id
     * @return the series with the specified id
     * @throws SeriesNotFoundException if the series does not exist
     */
    @Transactional
    public Series findById(String uuidString) {
        var uuid = UUIDUtils.convertFromString(uuidString)
                .orElseThrow(() -> new SeriesNotFoundException(uuidString));

        return this.findById(uuid);
    }

    /**
     * @param uuidString the series id
     * @param languages  the languages to filter
     * @return the series with the specified id and languages
     */
    @Transactional
    public TranslatedSeriesDTO findById(String uuidString, Set<Language> languages) {
        var entity = this.findById(uuidString);
        return this.seriesHelper.mapSeriesToTranslatedSeriesDTO(languages, entity);
    }

    /**
     * @param uuidString the series id
     *                   Deletes the series with the specified id
     * @throws SeriesNotFoundException if the series does not exist
     */
    @Transactional
    public void delete(String uuidString) {
        var entity = this.findById(uuidString);

        seriesRepository.delete(entity);
    }

    /**
     * @param uuidString the series id
     * @param seriesDTO  the series to be updated
     * @return the updated translated series with the specified id and languages
     */
    @Transactional
    public TranslatedSeriesDTO update(String uuidString, SeriesDTO seriesDTO) {
        var existingSeries = this.findById(uuidString);

        var languages = seriesDTO.languages().keySet();
        var seriesLanguagesDTOs = seriesDTO.languages().values();

        this.i18nSeriesService.validateDuplicateSlugInCollection(seriesLanguagesDTOs, existingSeries.getId());

        var updatedInternationalization = this.seriesHelper.updateI18nSeriesForSeries(existingSeries, seriesDTO, languages);

        existingSeries.setInternationalization(updatedInternationalization);
        var savedEntity = seriesRepository.save(existingSeries);

        return this.seriesHelper.mapSeriesToTranslatedSeriesDTO(languages, savedEntity);
    }

}
