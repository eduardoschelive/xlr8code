package com.xlr8code.server.series.service;

import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;
import com.xlr8code.server.series.dto.SeriesLanguageDTO;
import com.xlr8code.server.series.repository.I18nSeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class I18nSeriesService {

    private final I18nSeriesRepository i18nSeriesRepository;

    /**
     * @param seriesLanguageDTOS the i18n series to be validated
     * @throws SlugAlreadyExistsException if the slug already exists
     *  @throws DuplicateSlugInLanguagesException if the slugs are duplicated
     */
    @Transactional(readOnly = true)
    public void validateSlugInList(Collection<SeriesLanguageDTO> seriesLanguageDTOS) {
        this.validateDuplicateSlugs(seriesLanguageDTOS);
        List<String> slugs = seriesLanguageDTOS.stream()
                .map(SeriesLanguageDTO::slug)
                .toList();
        validateSlugInCollection(slugs);
    }

    /**
     * @param seriesLanguageDTOS the i18n series to be validated
     * @param seriesId  the series id to be validated
     * @throws DuplicateSlugInLanguagesException if the slugs are duplicated
     * @throws SlugAlreadyExistsException if the slug already exists and does not belong to the series id provided
     */
    @Transactional(readOnly = true)
    public void validateSlugInListWithOwner(Collection<SeriesLanguageDTO> seriesLanguageDTOS, UUID seriesId) {
        this.validateDuplicateSlugs(seriesLanguageDTOS);
        List<String> slugs = seriesLanguageDTOS.stream()
                .map(SeriesLanguageDTO::slug)
                .toList();
        validateSlugWithOwnerInCollection(slugs, seriesId);
    }

    /**
     * @param slug the slug to be validated
     */
    private void validateSlug(String slug) {
        if (i18nSeriesRepository.existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    /**
     * @param languages the languages to be validated
     * @throws DuplicateSlugInLanguagesException if the slugs are duplicated
     */
    private void validateDuplicateSlugs(Collection<SeriesLanguageDTO> languages) {
        Set<String> slugSet = new HashSet<>();
        for (SeriesLanguageDTO language : languages) {
            String slug = language.slug();
            if (!slugSet.add(slug)) {
                throw new DuplicateSlugInLanguagesException(slug);
            }
        }
    }

    /**
     * @param slugs the slugs to be validated
     */
    private void validateSlugInCollection(Collection<String> slugs) {
        slugs.forEach(this::validateSlug);
    }

    /**
     * @param slug    the slug to be validated
     * @param seriesId the series id to be validated
     * @throws SlugAlreadyExistsException if the slug already exists and does not belong to the series id provided
     */
    private void validateSlugWithOwner(String slug, UUID seriesId) {
        if (i18nSeriesRepository.existsBySlugAndSeriesIdNot(slug, seriesId)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    /**
     * @param slugs   the slugs to be validated
     * @param seriesId the series id to be validated
     */
    private void validateSlugWithOwnerInCollection(Collection<String> slugs, UUID seriesId) {
        slugs.forEach(slug -> validateSlugWithOwner(slug, seriesId));
    }

}
