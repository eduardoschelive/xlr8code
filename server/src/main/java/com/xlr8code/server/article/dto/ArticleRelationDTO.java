package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.common.utils.ObjectUtils;

import java.util.UUID;

public record ArticleRelationDTO(
        UUID previousArticleId,
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
