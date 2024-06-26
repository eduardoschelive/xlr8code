package com.xlr8code.server.filter.repository;

import com.xlr8code.server.filter.FilterSpecification;
import com.xlr8code.server.filter.utils.QueryParamsUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

public interface FilterRepository<E>  extends JpaSpecificationExecutor<E> {

    default Page<E> findAll(Map<String, String> queryParameters, Pageable pageable, Class<E> entityClass ) {

        QueryParamsUtils.removePageableParams(queryParameters);
        var query = new FilterSpecification<>(queryParameters, entityClass);

        return findAll(query, pageable);
    }

}
