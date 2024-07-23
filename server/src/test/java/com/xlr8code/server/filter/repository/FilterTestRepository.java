package com.xlr8code.server.filter.repository;


import com.xlr8code.server.filter.entity.FilterTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterTestRepository extends JpaRepository<FilterTestEntity, Long>, JpaSpecificationExecutor<FilterTestEntity> {
}
