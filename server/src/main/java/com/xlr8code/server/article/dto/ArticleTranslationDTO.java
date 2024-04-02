package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.I18nArticle;

public record ArticleTranslationDTO(
        String title,
        String slug,
        String content
) {
    public static ArticleTranslationDTO fromEntity(I18nArticle article) {
        return new ArticleTranslationDTO(article.getTitle(), article.getSlug(), article.getContent());
    }
}
