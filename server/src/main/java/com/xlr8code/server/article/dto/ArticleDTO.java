package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.annotation.ExistingArticle;
import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.category.annotation.ExistingCategory;
import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Map;
import java.util.stream.Collectors;

@Schema(name = "Article")
public record ArticleDTO(
        @Schema(description = "The category unique identifier")
        @ExistingCategory
        String categoryId,
        @Schema(description = "The previous article unique identifier")
        @ExistingArticle(optional = true)
        String previousArticleId,
        @Schema(description = "The next article unique identifier")
        @ExistingArticle(optional = true)
        String nextArticleId,
        @Schema(description = "The article position")
        @Positive
        Integer position,
        @Schema(description = "The article translations by language")
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
