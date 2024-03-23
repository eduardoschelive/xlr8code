package com.xlr8code.server.series.service;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.exception.PropertyDoesNotExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.series.dto.SeriesDTO;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import com.xlr8code.server.series.dto.TranslatedSeriesDTO;
import com.xlr8code.server.series.entity.I18nSeries;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final I18nSeriesService i18nSeriesService;

    @Resource
    @Lazy
    private SeriesService self;

    /**
     * @param seriesDTO the series to be created
     * @return the created series
     */
    @Transactional
    public Series create(SeriesDTO seriesDTO) {
        this.i18nSeriesService.validateSlugInList(seriesDTO.languages().values());

        var series = seriesDTO.toEntity();

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

        var languages = seriesDTO.languages().keySet();
        var seriesLanguagesDTOs = seriesDTO.languages().values();

        this.i18nSeriesService.validateSlugInListWithOwner(seriesLanguagesDTOs, existingSeries.getId());

        var updatedInternationalization = this.updateInternationalizationForSeries(existingSeries, seriesDTO, languages);

        existingSeries.getI18nSeries().clear();
        existingSeries.getI18nSeries().addAll(updatedInternationalization);

        var savedEntity = seriesRepository.save(existingSeries);

        return TranslatedSeriesDTO.fromEntity(savedEntity, languages);
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

    /**
     * @param series          the series to be updated
     * @param updateSeriesDTO the series to be updated
     * @param languages       the languages to be updated
     * @return the series languages updated to {@link I18nSeries}
     */
    private Set<I18nSeries> updateInternationalizationForSeries(Series series, SeriesDTO updateSeriesDTO, Set<Language> languages) {
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
     * @param series   the series to be updated
     * @param language the language to be updated
     * @return the series language to be updated
     */
    private I18nSeries findOrCreateI18NSeriesEntity(Series series, Language language) {
        return series.getI18nSeries().stream()
                .filter(i -> i.getLanguage().equals(language))
                .findFirst()
                .orElseGet(() -> I18nSeries.builder().series(series).language(language).build());
    }

    /**
     * @param i18n              the series language to be updated
     * @param seriesLanguageDTO the series language to be updated
     */
    private void updateI18NSeriesEntity(I18nSeries i18n, SeriesLanguageDTO seriesLanguageDTO) {
        i18n.setTitle(seriesLanguageDTO.title());
        i18n.setDescription(seriesLanguageDTO.description());
        i18n.setSlug(seriesLanguageDTO.slug());
    }

}
