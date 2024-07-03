package com.xlr8code.server.filter.repository;

import com.xlr8code.server.filter.entity.FilterOneToOneRelationTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterOneToOneRelationRepository extends JpaRepository<FilterOneToOneRelationTest, Long> {
}
