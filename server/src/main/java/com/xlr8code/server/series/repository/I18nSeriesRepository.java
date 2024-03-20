package com.xlr8code.server.series.repository;

import com.xlr8code.server.series.entity.I18nSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface I18nSeriesRepository extends JpaRepository<I18nSeries, UUID> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndSeriesIdNot(String slug, UUID id);

}
