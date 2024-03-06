package com.xlr8code.server.series.repository;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface I18nSeriesRepository extends JpaRepository<I18nSeries, UUID> {

    boolean existsBySeriesAndLanguage(Series series, Language language);

    boolean existsBySlug(String slug);

}
