package com.xlr8code.server.article.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ArticleRelation {

    @OneToOne
    @JoinColumn(name = "previous_article_id")
    private Article previousArticle;

    @OneToOne
    @JoinColumn(name = "next_article_id")
    private Article nextArticle;

}
