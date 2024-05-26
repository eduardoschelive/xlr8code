package com.xlr8code.server.series.repository;

import com.xlr8code.server.series.entity.I18nSeries;
import com.xlr8code.server.series.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface I18nSeriesRepository extends JpaRepository<I18nSeries, UUID> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndSeriesNot(String slug, Series owner);
}
