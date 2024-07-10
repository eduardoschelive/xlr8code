package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.annotation.ExistingArticle;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.category.annotation.ExistingCategory;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.enums.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Map;
import java.util.stream.Collectors;

public record ArticleDTO(
        @ExistingCategory(optional = true)
        String categoryId,
        @ExistingArticle(optional = true)
        String previousArticleId, @ExistingArticle(optional = true)
        String nextArticleId,
        @Positive
        Integer position,
        @NotEmpty
        Map<Language, @Valid ArticleLanguageDTO> languages
) {

    public Article toEntity(Article article, Category category, ArticleRelation articleRelation) {
        article.setPosition(this.position());
        article.setCategory(category);
        article.setArticleRelation(articleRelation);

        var articleLanguages = this.languages().entrySet().stream()
                .map(entry -> entry.getValue().toEntity(article, entry.getKey()))
                .collect(Collectors.toSet());

        article.setI18nArticles(articleLanguages);

        return article;
    }

}
