package com.xlr8code.server.series.service;

import com.xlr8code.server.common.validation.SlugValidator;
import com.xlr8code.server.series.entity.Series;
import com.xlr8code.server.series.repository.I18nSeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeriesSlugValidator implements SlugValidator<Series> {

    private final I18nSeriesRepository i18nSeriesRepository;


    @Override
    public boolean existsBySlug(String slug) {
        return i18nSeriesRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsBySlugAndOwnerNot(String slug, Series ownerEntity) {
        return i18nSeriesRepository.existsBySlugAndSeriesNot(slug, ownerEntity);
    }
}
