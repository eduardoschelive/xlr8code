package com.xlr8code.server.article.repository;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.filter.repository.FilterRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID>, FilterRepository<Article> {
}
