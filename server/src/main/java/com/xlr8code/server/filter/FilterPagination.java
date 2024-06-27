package com.xlr8code.server.filter;

import com.xlr8code.server.filter.exception.PageNumberFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.PAGE_PARAM;
import static com.xlr8code.server.filter.utils.FilterConstants.SIZE_PARAM;

@RequiredArgsConstructor
public class FilterPagination {

    private final Map<String, String> paginationParams;

    public PageRequest getPageRequest() {
        int page = this.parseIntegerParameter(PAGE_PARAM);
        int size = this.parseIntegerParameter(SIZE_PARAM);
        return PageRequest.of(page, size);
    }

    public int parseIntegerParameter(String parameterName) {
        String value = paginationParams.get(parameterName);
        if (value == null) {
            throw new PageNumberFormatException(null);
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
