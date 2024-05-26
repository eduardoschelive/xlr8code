package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.PropertyDoesNotExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.exception.SeriesNotFoundException;
import com.xlr8code.server.series.repository.SeriesRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesSlugValidator seriesSlugValidator;

    @Resource
    @Lazy
    private SeriesService self;

    /**
     * @param seriesDTO the series to be created
     * @return the created series
     */
    @Transactional
    public Series create(SeriesDTO seriesDTO) {
        this.seriesSlugValidator.validateSlugs(seriesDTO.languages().values());
        var emptySeries = new Series();
        var series = seriesDTO.toEntity(emptySeries);
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
            var filteredTranslatedSeriesDTOs = this.mapAndFilterEmptyLanguages(languages, seriesPage.getContent());

            return new PageImpl<>(filteredTranslatedSeriesDTOs, pageable, seriesPage.getTotalElements());
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

        var filteredTranslatedSeriesDTOs = this.mapAndFilterEmptyLanguages(languages, page.getContent());

        return new PageImpl<>(filteredTranslatedSeriesDTOs, pageable, page.getTotalElements());
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

        return self.findById(uuid);
    }

    /**
     * @param uuidString the series id
     * @param languages  the languages to filter
     * @return the series with the specified id and languages
     */
    @Transactional
    public TranslatedSeriesDTO findById(String uuidString, Set<Language> languages) {
        var entity = self.findById(uuidString);
        return TranslatedSeriesDTO.fromEntity(entity, languages);
    }

    /**
     * @param uuidString the series id
     * @throws SeriesNotFoundException if the series does not exist
     */
    @Transactional
    public void delete(String uuidString) {
        var entity = self.findById(uuidString);
        seriesRepository.delete(entity);
    }

    /**
     * @param uuidString the series id
     * @param seriesDTO  the series to be updated
     * @return the updated translated series with the specified id and languages
     */
    @Transactional
    public TranslatedSeriesDTO update(String uuidString, SeriesDTO seriesDTO) {
        var existingSeries = self.findById(uuidString);
        this.seriesSlugValidator.validateSlugs(seriesDTO, existingSeries);

        var updatedSeries = seriesDTO.toEntity(existingSeries);
        var savedEntity = seriesRepository.save(updatedSeries);

        return TranslatedSeriesDTO.fromEntity(savedEntity);
    }

    /**
     * @param id the series id
     * @return whether the series exists
     */
    @Transactional
    public boolean existsById(String id) {
        var uuid = UUIDUtils.convertFromString(id);
        return uuid.filter(this.seriesRepository::existsById).isPresent();
    }

    /**
     * @param languages  the languages to filter
     * @param seriesList the series to be mapped
     * @return the series languages with non-empty languages
     */
    private List<TranslatedSeriesDTO> mapAndFilterEmptyLanguages(Set<Language> languages, List<Series> seriesList) {
        var seriesLanguagesDTOList = this.mapSeriesToTranslatedSeriesDTO(languages, seriesList);
        return this.filterEmptyLanguages(seriesLanguagesDTOList);
    }

    /**
     * @param languages  the languages to be filtered
     * @param seriesList the series to be mapped
     * @return the series languages with non-empty languages
     */
    private List<TranslatedSeriesDTO> mapSeriesToTranslatedSeriesDTO(Set<Language> languages, List<Series> seriesList) {
        return seriesList.stream()
                .map(s -> TranslatedSeriesDTO.fromEntity(s, languages))
                .toList();
    }

    /**
     * @param seriesLanguagesDTOList the series languages to be filtered
     * @return the series languages with non-empty languages
     */
    private List<TranslatedSeriesDTO> filterEmptyLanguages(List<TranslatedSeriesDTO> seriesLanguagesDTOList) {
        return seriesLanguagesDTOList.stream()
                .filter(dto -> !dto.languages().isEmpty())
                .toList();
    }

}
