package com.xlr8code.server.filter.repository;

import com.xlr8code.server.filter.Filter;
import com.xlr8code.server.filter.exception.NoMatchingEntitiesFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

/**
 * @param <E> the entity type
 */
public interface FilterRepository<E> extends JpaSpecificationExecutor<E> {

    /**
     * @param queryParameters all the request parameters
     * @param entityClass     the target entity class
     * @return the page of entities that match the query parameters
     */
    default Page<E> findAll(Map<String, String> queryParameters, Class<E> entityClass) {
        var filter = new Filter<>(queryParameters, entityClass);
        var specification = filter.getSpecification();
        var pageRequest = filter.getPageRequest();

        var result = findAll(specification, pageRequest);

        if (result.isEmpty()) {
            throw new NoMatchingEntitiesFoundException();
        }

        return result;
    }
}
