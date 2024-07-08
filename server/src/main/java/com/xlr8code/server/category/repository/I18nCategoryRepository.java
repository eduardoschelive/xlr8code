package com.xlr8code.server.category.repository;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.category.entity.I18nCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface I18nCategoryRepository extends JpaRepository<I18nCategory, UUID> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndCategoryNot(String slug, Category owner);
}
