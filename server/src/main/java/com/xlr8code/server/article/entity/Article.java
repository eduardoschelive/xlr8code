package com.xlr8code.server.article.entity;

import com.xlr8code.server.common.entity.AuditableEntity;
import com.xlr8code.server.series.entity.Series;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "article_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(name = "position")
    private Integer position;

    @Embedded
    private ArticleRelation articleRelation;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private Set<I18nArticle> i18nArticles;

}