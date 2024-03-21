package com.xlr8code.server.series.repository;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.entity.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<Series, UUID> {

    @Query("SELECT s FROM Series s " +
            "LEFT JOIN s.i18nSeries i18n " +
            "WHERE i18n.language IN :languages " +
            "AND (LOWER(i18n.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "     OR LOWER(i18n.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY i18n.title DESC")
    Page<Series> search(String query, Set<Language> languages, Pageable seriesPage);

}
