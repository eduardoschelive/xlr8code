package com.xlr8code.server.filter.utils;

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

    public void createNEntities(int n) {
        IntStream.range(0, n).forEach(i -> {
            FilterTestEntity entity = createAndSaveFilterTestEntity(i);
            IntStream.range(0, n).forEach(j -> createAndSaveFilterRelationTest(entity, i, j));
            createAndSaveFilterOneToOneRelationTest(entity, i);
        });
    }

    public void clearRepositories() {
        testRepository.deleteAll();
        relationTestRepository.deleteAll();
        oneToOneRelationRepository.deleteAll();
    }

    private FilterTestEntity createAndSaveFilterTestEntity(int index) {
        return testRepository.save(FilterTestEntity.builder()
                .stringField("stringField" + index)
                .booleanField(index % 2 == 0)
                .build());
    }

    private void createAndSaveFilterRelationTest(FilterTestEntity entity, int i, int j) {
        relationTestRepository.save(FilterRelationTest.builder()
                .testEntity(entity)
                .stringRelationField("stringField" + i + j)
                .booleanRelationField(j % 2 == 0)
                .build());
    }

    private void createAndSaveFilterOneToOneRelationTest(FilterTestEntity entity, int i) {
        oneToOneRelationRepository.save(FilterOneToOneRelationTest.builder()
                .testEntity(entity)
                .stringRelationField("stringField" + i)
                .booleanRelationField(i % 2 == 0)
                .build());
    }

}
