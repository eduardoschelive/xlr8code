package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.I18nArticle;
import com.xlr8code.server.common.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(name = "TranslatedArticle")
public record TranslatedArticleDTO(
        @Schema(description = "The article unique identifier")
        UUID id,
        @Schema(description = "The article relation information")
        ArticleRelationDTO articleRelation,
        @Schema(description = "The article translations")
        Map<Language, ArticleTranslationDTO> languages,
        @Schema(description = "The article creation date")
        Instant createdAt
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

        return new TranslatedArticleDTO(article.getId(), articleRelation, articleLanguages, article.getCreatedAt());
    }

}
