package com.xlr8code.server.filter;

import com.xlr8code.server.filter.exception.InvalidSortDirectionException;
import com.xlr8code.server.filter.exception.NoSuchSortableFieldException;
import com.xlr8code.server.filter.utils.FilterableFieldDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.ACCEPTED_SORT_VALUES;
import static com.xlr8code.server.filter.utils.FilterConstants.SEARCH_PARAM_SEPARATOR;

@RequiredArgsConstructor
public class FilterSorting {

    private final Map<String, String> sortingParams;
    private final Map<String, FilterableFieldDetails> filterDetailsMap;


    public Sort getSort() {
        var sortBuilder = Sort.unsorted();

        for (Map.Entry<String, String> entry : sortingParams.entrySet()) {
            String key = extractFieldPath(entry.getKey());
            String value = entry.getValue();

            if (!isSortField(key)) {
                throw new NoSuchSortableFieldException(key);
            }

            if (!isValidSortOrder(value)) {
                throw new InvalidSortDirectionException(value);
            }

            var field = filterDetailsMap.get(key).fieldPath();
            var direction = Sort.Direction.fromString(value);
            sortBuilder = sortBuilder.and(Sort.by(direction, field));
        }

        return sortBuilder;
    }

    private boolean isSortField(String key) {
        return filterDetailsMap.containsKey(key);
    }

    private boolean isValidSortOrder(String order) {
        return ACCEPTED_SORT_VALUES.contains(order);
    }

    // TODO: reuse of function in FilterPagination.java
    private String extractFieldPath(String key) {
        int separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(0, separatorIndex).trim() : null;
    }

}
