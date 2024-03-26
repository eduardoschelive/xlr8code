package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.I18nArticle;
import com.xlr8code.server.common.enums.Language;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record ArticleLanguageDTO(
        @NotBlank
        String title,
        @NotBlank
        String slug,
        @NotBlank
        String content
) {

    public I18nArticle toEntity(Article article, Language language) {
        var i18nArticle = findI18nArticle(article, language).orElseGet(I18nArticle::new);

        i18nArticle.setArticle(article);
        i18nArticle.setLanguage(language);
        i18nArticle.setTitle(this.title());
        i18nArticle.setSlug(this.slug());
        i18nArticle.setContent(this.content());

        return i18nArticle;
    }

    private Optional<I18nArticle> findI18nArticle(Article article, Language language) {
        return Optional.ofNullable(article.getI18nArticles())
                .flatMap(i18nArticles -> i18nArticles.stream()
                        .filter(i18n -> i18n.getLanguage().equals(language))
                        .findFirst());
    }

}
