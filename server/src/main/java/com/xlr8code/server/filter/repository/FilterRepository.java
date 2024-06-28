package com.xlr8code.server.filter.repository;

import com.xlr8code.server.filter.FilterPagination;
import com.xlr8code.server.filter.FilterSorting;
import com.xlr8code.server.filter.FilterSpecification;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.filter.utils.QueryParameterDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

public interface FilterRepository<E> extends JpaSpecificationExecutor<E> {

    default Page<E> findAll(Map<String, String> queryParameters, Class<E> entityClass) {
        var queryParamDetails = new QueryParameterDetails(queryParameters);
        var filterDetailsMap = FilterUtils.extractFilterableFields(entityClass);

        var query = new FilterSpecification<E>(queryParamDetails.getFilterParameters(), filterDetailsMap);
        var sort = new FilterSorting(queryParamDetails.getSortParameters(), filterDetailsMap);
        var pagination = new FilterPagination(queryParamDetails.getPaginationParameters());

        var pageRequestWithSort = pagination.getPageRequest().withSort(sort.getSort());

        return findAll(query, pageRequestWithSort);
    }

}
