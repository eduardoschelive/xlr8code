package com.xlr8code.server.series.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "series_id", nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "series")
    private Set<I18nSery> i18nSeries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "series")
    private Set<Section> sections = new LinkedHashSet<>();

}