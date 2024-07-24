package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.I18nArticle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ArticleTranslation")
public record ArticleTranslationDTO(
        @Schema(description = "The article title")
        String title,
        @Schema(description = "The article slug")
        String slug,
        @Schema(description = "The article content")
        String content
) {
    public static ArticleTranslationDTO fromEntity(I18nArticle article) {
        return new ArticleTranslationDTO(article.getTitle(), article.getSlug(), article.getContent());
    }
}
