package com.xlr8code.server.section.entity;

import com.xlr8code.server.article.entity.Article;
import com.xlr8code.server.series.entity.Series;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "section_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @NotNull
    @Column(name = "order_within_series", nullable = false)
    private Short orderWithinSeries;

    @OneToMany(mappedBy = "section")
    private Set<Article> articles;

    @OneToMany(mappedBy = "section")
    private Set<I18nSection> i18nSections;

}