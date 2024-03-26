package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.annotation.ExistingArticle;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.series.annotation.ExistingSeries;
import com.xlr8code.server.series.entity.Series;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Map;
import java.util.stream.Collectors;

public record ArticleDTO(
        @ExistingSeries(optional = true)
        String seriesId,
        @ExistingArticle(optional = true)
        String nextArticleId,
        @ExistingArticle(optional = true)
        String previousArticleId,
        @ExistingArticle(optional = true)
        String parentArticleId,
        @Positive
        Integer position,
        @NotEmpty
        Map<Language, @Valid ArticleLanguageDTO> languages
) {

    public Article toEntity(Article article, Series series, ArticleRelation articleRelation) {
        article.setPosition(this.position());
        article.setSeries(series);
        article.setArticleRelation(articleRelation);

        var articleLanguages = this.languages().entrySet().stream()
                .map(entry -> entry.getValue().toEntity(article, entry.getKey()))
                .collect(Collectors.toSet());

        article.setI18nArticles(articleLanguages);

        return article;
    }

}
