package com.xlr8code.server.article.repository;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.I18nArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface I18nArticleRepository extends JpaRepository<I18nArticle, UUID> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndArticleNot(String slug, Article article);
}
