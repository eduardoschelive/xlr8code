package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.I18nArticle;
import com.xlr8code.server.common.enums.Language;
import jakarta.validation.constraints.NotBlank;

public record ArticleLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String content
) {

    public I18nArticle toEntity(Article article, Language language) {
        return I18nArticle.builder()
                .title(this.title())
                .slug(this.slug())
                .language(language)
                .content(this.content())
                .article(article)
                .build();
    }

}
