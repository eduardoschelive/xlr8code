package com.xlr8code.server.category.repository;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT s FROM Category s " +
            "LEFT JOIN s.i18nCategories i18n " +
            "WHERE i18n.language IN :languages " +
            "AND (LOWER(i18n.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "     OR LOWER(i18n.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY i18n.title DESC")
    Page<Category> search(String query, Set<Language> languages, Pageable seriesPage);

}
