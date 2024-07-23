package com.xlr8code.server.filter.utils;

import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.FilterPagination;
import com.xlr8code.server.filter.FilterSorting;
import com.xlr8code.server.filter.FilterSpecification;
import com.xlr8code.server.filter.entity.FilterOneToOneRelationTest;
import com.xlr8code.server.filter.entity.FilterRelationTest;
import com.xlr8code.server.filter.entity.FilterTestEntity;
import com.xlr8code.server.filter.repository.FilterOneToOneRelationRepository;
import com.xlr8code.server.filter.repository.FilterRelationTestRepository;
import com.xlr8code.server.filter.repository.FilterTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FilterTestUtils {

    private final FilterTestRepository testRepository;
    private final FilterRelationTestRepository relationTestRepository;
    private final FilterOneToOneRelationRepository oneToOneRelationRepository;

    public void createTestEntity(String stringField, Boolean booleanField, Theme themeField) {
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

    public Specification<FilterTestEntity> buildSpecification(Map<String, String> filters) {
        var filterDetails = FilterUtils.extractFilterableFields(FilterTestEntity.class);
        return new FilterSpecification<>(filters, filterDetails);
    }

    public PageRequest buildPageable(Map<String, String> filters) {
        var filterDetails = FilterUtils.extractFilterableFields(FilterTestEntity.class);
        var queryParameters = new  QueryParameterDetails(filters);

        var sort = new FilterSorting(queryParameters.getSortParameters(), filterDetails);
        var pagination = new FilterPagination(queryParameters.getPaginationParameters());

        return pagination.getPageRequest().withSort(sort.getSort());
    }

}
