package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.common.utils.ObjectUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "ArticleRelation")
public record ArticleRelationDTO(
        @Schema(description = "The previous article unique identifier")
        UUID previousArticleId,
        @Schema(description = "The next article unique identifier")
        UUID nextArticleId
) {

    public static ArticleRelationDTO fromEntity(ArticleRelation articleRelation) {
        var previousArticle = ObjectUtils.executeIfNotNull(articleRelation, ArticleRelation::getPreviousArticle);
        var nextArticle = ObjectUtils.executeIfNotNull(articleRelation, ArticleRelation::getNextArticle);

        return new ArticleRelationDTO(
                ObjectUtils.executeIfNotNull(previousArticle, Article::getId),
                ObjectUtils.executeIfNotNull(nextArticle, Article::getId)
        );
    }

}
