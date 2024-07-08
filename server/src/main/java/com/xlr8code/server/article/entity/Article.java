package com.xlr8code.server.article.entity;

import com.xlr8code.server.category.entity.Category;
import com.xlr8code.server.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "articles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "article_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "position")
    private Integer position;

    @Embedded
    private ArticleRelation articleRelation;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private Set<I18nArticle> i18nArticles;

}