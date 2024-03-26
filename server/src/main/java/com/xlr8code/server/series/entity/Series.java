package com.xlr8code.server.series.entity;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "series")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Series extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "series_id", nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private Set<I18nSeries> i18nSeries;

    @OneToMany
    @JoinColumn(name = "series_id")
    private Set<Article> articles;

}