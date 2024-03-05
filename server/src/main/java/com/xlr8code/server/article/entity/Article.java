package com.xlr8code.server.series.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "article_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @NotNull
    @Column(name = "order_within_section", nullable = false)
    private Short orderWithinSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_post_id")
    private Article previousPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_post_id")
    private Article nextPost;

    @OneToMany(mappedBy = "article")
    private Set<I18nArticle> i18nArticles = new LinkedHashSet<>();

}