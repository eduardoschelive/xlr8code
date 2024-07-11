package com.xlr8code.server.filter.entity;

import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.annotation.Filterable;
import com.xlr8code.server.filter.annotation.NestedFilterable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "filter_test_table")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class FilterTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Filterable
    @Column(name = "stringField", nullable = false)
    private String stringField;

    @Filterable
    @Column(name = "booleanField", nullable = false)
    private Boolean booleanField;

    @Filterable
    @Column(name = "enumThemeField", nullable = false)
    @Enumerated(EnumType.STRING)
    private Theme enumThemeField;

    @NestedFilterable
    @OneToMany(mappedBy = "testEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilterRelationTest> testRelationEntity;

    @NestedFilterable
    @OneToOne(mappedBy = "testEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private FilterOneToOneRelationTest testOneToOneRelationEntity;

}
