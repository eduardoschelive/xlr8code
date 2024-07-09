package com.xlr8code.server.category.repository;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.filter.repository.FilterRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>, FilterRepository<Category> {
}
