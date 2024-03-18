package com.xlr8code.server.series.service;

import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.common.utils.UUIDUtils;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.repository.I18nSeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class I18nSeriesService {

    private final I18nSeriesRepository i18nSeriesRepository;

    /**
     * @param i18nSeries the list of series to be validated
     * @throws DuplicateSlugInLanguagesException if the slug already exists in the list
     */
    public void validateDuplicateSlugs(Set<I18nSeries> i18nSeries) {
        Set<String> slugSet = new HashSet<>();
        for (I18nSeries series : i18nSeries) {
            var slug = series.getSlug();
            if (!slugSet.add(slug)) {
                throw new DuplicateSlugInLanguagesException(slug);
            }
        }
    }

    /**
     * @param i18nSeries the list of series to be validated
     * @throws SlugAlreadyExistsException if the slug already exists in the database
     */
    @Transactional(readOnly = true)
    public void validateSlugsInDatabase(Set<I18nSeries> i18nSeries) {
        for (I18nSeries series : i18nSeries) {
            var slug = series.getSlug();
            var id =  series.getSeries().getId();
            boolean existsBySlugAndSeriesIdNot = id == null ? i18nSeriesRepository.existsBySlug(slug) : i18nSeriesRepository.existsBySlugAndSeriesIdNot(slug, id);
            if (existsBySlugAndSeriesIdNot){
                throw new SlugAlreadyExistsException(slug);
            }
        }
    }

    /**
     * @param i18nSeries the list of series to be validated
     * @throws DuplicateSlugInLanguagesException if the slug already exists in the list
     * @throws SlugAlreadyExistsException        if the slug already exists in the database
     */
    @Transactional(readOnly = true)
    public void validateSlugInList(Set<I18nSeries> i18nSeries) {
        validateDuplicateSlugs(i18nSeries);
        validateSlugsInDatabase(i18nSeries);
    }

    @Transactional
    public void deleteAllBySeriesId(UUID seriesId) {
        i18nSeriesRepository.deleteAllBySeriesId(seriesId);
    }

}
