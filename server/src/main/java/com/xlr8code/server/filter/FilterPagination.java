package com.xlr8code.server.filter;

import com.xlr8code.server.filter.exception.PageNumberFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

@RequiredArgsConstructor
public class FilterPagination {

    private final Map<String, String> paginationParams;

    public PageRequest getPageRequest() {
        int page = this.parseIntegerParameter(PAGE_PARAM, DEFAULT_PAGE);
        int size = this.parseIntegerParameter(SIZE_PARAM, DEFAULT_SIZE);

        return PageRequest.of(page, size);
    }

    private int parseIntegerParameter(String parameterName, int defaultValue) {
        var value = paginationParams.get(parameterName);
        if (value == null) {
            return defaultValue;
        }
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0) {
                throw new PageNumberFormatException(value);
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new PageNumberFormatException(value);
        }
    }

}
