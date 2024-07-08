package com.xlr8code.server.filter.utils;

import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.entity.FilterOneToOneRelationTest;
import com.xlr8code.server.filter.entity.FilterRelationTest;
import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.repository.FilterOneToOneRelationRepository;
import com.xlr8code.server.filter.repository.FilterRelationTestRepository;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FilterTestUtils {

    private final FilterTestRepository testRepository;
    private final FilterRelationTestRepository relationTestRepository;
    private final FilterOneToOneRelationRepository oneToOneRelationRepository;

    public void createTestEntity(String stringField, boolean booleanField, Theme themeField) {
        var entity = FilterTestEntity.builder()
                .stringField(stringField)
                .booleanField(booleanField)
                .enumThemeField(themeField)
                .build();

        testRepository.save(entity);
    }

    public void createTestEntityWithRelation(String stringField, boolean booleanField, Theme themeField) {
        var entity = FilterTestEntity.builder()
                .stringField(stringField)
                .booleanField(booleanField)
                .enumThemeField(themeField)
                .build();

        var newEntity = testRepository.save(entity);

        var relationEntity = FilterRelationTest.builder()
                .testEntity(newEntity)
                .booleanRelationField(booleanField)
                .stringRelationField(stringField)
                .build();

        relationTestRepository.save(relationEntity);

        var oneToOneRelationEntity = FilterOneToOneRelationTest.builder()
                .testEntity(newEntity)
                .booleanRelationField(booleanField)
                .stringRelationField(stringField)
                .build();

        oneToOneRelationRepository.save(oneToOneRelationEntity);
    }

    public void clearRepositories() {
        testRepository.deleteAll();
        relationTestRepository.deleteAll();
        oneToOneRelationRepository.deleteAll();
    }

}
