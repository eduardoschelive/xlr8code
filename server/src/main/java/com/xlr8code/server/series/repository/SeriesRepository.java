package com.xlr8code.server.series.repository;

import com.xlr8code.server.series.entity.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<Series, UUID> {

    Page<Series> findAll(Pageable pageable);

}
