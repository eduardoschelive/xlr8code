package com.xlr8code.server.filter;

import com.xlr8code.server.common.utils.PageUtils;
import com.xlr8code.server.filter.exception.PageNumberFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

@RequiredArgsConstructor
public class FilterPagination {

    private final Map<String, String> paginationParams;

    public PageRequest getPageRequest() {
        var page = parseIntegerParameter(PAGE_PARAM, DEFAULT_PAGE);
        var size = parseIntegerParameter(SIZE_PARAM, DEFAULT_SIZE);

        return PageRequest.of(page, size);
    }

    private int parseIntegerParameter(String parameterName, int defaultValue) {
        var value = paginationParams.get(parameterName);
        if (value == null) {
            return defaultValue;
        }
        return parseValue(value);
    }

    private int parseValue(String value) {
        try {
            var parsedValue = Integer.parseInt(value);
            var zeroIndexValue = PageUtils.zeroIndexPage(parsedValue);

            if (zeroIndexValue < 0) {
                throw new PageNumberFormatException(value);
            }

            return zeroIndexValue;
        } catch (NumberFormatException e) {
            throw new PageNumberFormatException(value);
        }
    }

}
