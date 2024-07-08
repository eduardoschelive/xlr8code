package com.xlr8code.server.filter.entity;

import com.xlr8code.server.filter.annotation.Filterable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "filter_one_to_one_relation_test_table")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class FilterOneToOneRelationTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Filterable
    @Column(name = "stringRelationField", nullable = false)
    private String stringRelationField;

    @Filterable
    @Column(name = "booleanRelationField", nullable = false)
    private boolean booleanRelationField;

    @OneToOne
    @JoinColumn(name = "filter_test_entity_id", nullable = false)
    private FilterTestEntity testEntity;

}
