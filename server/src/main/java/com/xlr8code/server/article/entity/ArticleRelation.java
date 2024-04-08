package com.xlr8code.server.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ArticleRelation {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_article_id")
    private Article previousArticle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_article_id")
    private Article nextArticle;

}
