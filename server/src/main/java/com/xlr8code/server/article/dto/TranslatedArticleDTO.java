package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.I18nArticle;
import com.xlr8code.server.common.enums.Language;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TranslatedArticleDTO(
        UUID id,
        ArticleRelationDTO articleRelation,
        Map<Language, ArticleTranslationDTO> languages
) {

    public static TranslatedArticleDTO fromEntity(Article article, Set<Language> languages) {
        var articleLanguages = article.getI18nArticles().stream()
                .filter(i18n -> languages.contains(i18n.getLanguage()))
                .collect(Collectors.toMap(
                        I18nArticle::getLanguage,
                        ArticleTranslationDTO::fromEntity,
                        (existing, replacement) -> existing
                ));

        var articleRelation = ArticleRelationDTO.fromEntity(article.getArticleRelation());

        return new TranslatedArticleDTO(article.getId(), articleRelation, articleLanguages);
    }

}
