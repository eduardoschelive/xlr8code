package com.xlr8code.server.series.entity;

import com.xlr8code.server.common.entity.AuditableEntity;
import com.xlr8code.server.section.entity.Section;
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

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<I18nSeries> i18nSeries;

    @OneToMany(mappedBy = "series")
    private Set<Section> sections;

}