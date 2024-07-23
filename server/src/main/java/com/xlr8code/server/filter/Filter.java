package com.xlr8code.server.filter;

import com.xlr8code.server.filter.utils.FilterFieldDetails;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.filter.utils.QueryParameterDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class Filter<E> {

    private final QueryParameterDetails queryParameters;
    private final Map<String, FilterFieldDetails> filterDetails;


    public Filter(Map<String, String> queryParameters, Class<E> entityClass) {
        this.queryParameters = new QueryParameterDetails(queryParameters);
        this.filterDetails = FilterUtils.extractFilterableFields(entityClass);
    }

    public FilterSpecification<E> getSpecification() {
        return new FilterSpecification<>(this.queryParameters.getFilterParameters(), this.filterDetails);
    }

    public PageRequest getPageRequest() {
        var sort = new FilterSorting(this.queryParameters.getSortParameters(), this.filterDetails);
        var pagination = new FilterPagination(this.queryParameters.getPaginationParameters());

        return pagination.getPageRequest().withSort(sort.getSort());
    }

}
