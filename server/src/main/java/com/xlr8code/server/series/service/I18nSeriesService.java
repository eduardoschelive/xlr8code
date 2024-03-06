package com.xlr8code.server.series.service;

import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.LanguageAlreadyExistsForResource;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.repository.I18nSeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class I18nSeriesService {

    private final I18nSeriesRepository i18nSeriesRepository;

    /**
     * @param i18nSeries the series to which the I18nSeries will be associated
     * @return the created I18nSeries
     */
    @Transactional
    public I18nSeries create(I18nSeries i18nSeries) {
        this.validateLanguageForSeries(i18nSeries);
        this.validateSlug(i18nSeries.getSlug());

        return i18nSeriesRepository.save(i18nSeries);
    }

    /**
     * @param i18nSeries the series to which the I18nSeries will be associated
     * @return the created I18nSeries
     */
    @Transactional
    public List<I18nSeries> create(List<I18nSeries> i18nSeries) {
        i18nSeries.forEach(this::validateLanguageForSeries);
        this.validateSlugInList(i18nSeries);
        return i18nSeriesRepository.saveAll(i18nSeries);
    }

    /**
     * @param slug the slug to be validated
     * @throws SlugAlreadyExistsException if the slug already exists
     */
    @Transactional
    public void validateSlug(String slug) {
        if (i18nSeriesRepository.existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    /**
     * @param i18nSeries the list of series to be validated
     * @throws DuplicateSlugInLanguagesException if the slug already exists
     * @throws SlugAlreadyExistsException        if the slug already exists in the database
     */
    @Transactional
    public void validateSlugInList(List<I18nSeries> i18nSeries) {
        Set<String> slugSet = new HashSet<>();
        for (I18nSeries series : i18nSeries) {
            var slug = series.getSlug();
            if (!slugSet.add(slug)) {
                throw new DuplicateSlugInLanguagesException(slug);
            }
            this.validateSlug(slug);
        }
    }

    /**
     * @param i18nSeries the series to be validated
     * @throws LanguageAlreadyExistsForResource if the language already exists for the series
     */
    @Transactional
    public void validateLanguageForSeries(I18nSeries i18nSeries) {
        if (i18nSeriesRepository.existsBySeriesAndLanguage(i18nSeries.getSeries(), i18nSeries.getLanguage())) {
            throw new LanguageAlreadyExistsForResource(i18nSeries.getLanguage(), i18nSeries.getSeries().getId());
        }
    }

}
