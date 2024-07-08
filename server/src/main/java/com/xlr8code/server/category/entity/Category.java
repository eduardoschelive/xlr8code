package com.xlr8code.server.category.entity;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "category")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id", nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<I18nCategory> i18nCategories;

    @OneToMany
    @JoinColumn(name = "category_id")
    private Set<Article> articles;

}