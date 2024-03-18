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

    @Transactional(readOnly = true)
    public void validateSlugInList(Collection<SeriesLanguageDTO> i18nSeries) {
        this.validateDuplicateSlugs(i18nSeries);
        List<String> slugs = i18nSeries.stream()
                .map(SeriesLanguageDTO::slug)
                .toList();
        validateSlugInCollection(slugs);
    }

    @Transactional(readOnly = true)
    public void validateSlugInListWithOwner(Collection<SeriesLanguageDTO> i18nSeries, UUID seriesId) {
        this.validateDuplicateSlugs(i18nSeries);
        List<String> slugs = i18nSeries.stream()
                .map(SeriesLanguageDTO::slug)
                .toList();
        validateSlugWithOwnerInCollection(slugs, seriesId);
    }

    private void validateSlug(String slug) {
        if (i18nSeriesRepository.existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    private void validateDuplicateSlugs(Collection<SeriesLanguageDTO> languages) {
        Set<String> slugSet = new HashSet<>();
        for (SeriesLanguageDTO language : languages) {
            String slug = language.slug();
            if (!slugSet.add(slug)) {
                throw new DuplicateSlugInLanguagesException(slug);
            }
        }
    }

    private void validateSlugInCollection(Collection<String> slugs) {
        slugs.forEach(this::validateSlug);
    }

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
