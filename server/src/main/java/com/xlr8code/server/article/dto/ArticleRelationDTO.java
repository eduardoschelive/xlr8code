package com.xlr8code.server.article.dto;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.article.entity.ArticleRelation;
import com.xlr8code.server.common.utils.ObjectUtils;

import java.util.UUID;

public record ArticleRelationDTO(
        UUID parentArticleId,
        UUID previousArticleId,
        UUID nextArticleId
) {

    public static ArticleRelationDTO fromEntity(ArticleRelation articleRelation) {
        return new ArticleRelationDTO(
                ObjectUtils.executeIfNotNull(articleRelation.getParentArticle(), Article::getId),
                ObjectUtils.executeIfNotNull(articleRelation.getPreviousArticle(), Article::getId),
                ObjectUtils.executeIfNotNull(articleRelation.getNextArticle(), Article::getId)
        );
    }

}
