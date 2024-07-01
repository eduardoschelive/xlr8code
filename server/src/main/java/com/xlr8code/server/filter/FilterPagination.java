package com.xlr8code.server.filter;

import com.xlr8code.server.common.utils.PageUtils;
import com.xlr8code.server.filter.exception.PageNumberFormatException;
import com.xlr8code.server.filter.exception.PageSizeFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.function.Function;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

@RequiredArgsConstructor
public class FilterPagination {

    private final Map<String, String> paginationParams;

    public PageRequest getPageRequest() {
        var page = parseIntegerParameter(PAGE_PARAM, DEFAULT_PAGE, PageNumberFormatException::new);
        validatePageNumber(page);

        var size = parseIntegerParameter(SIZE_PARAM, DEFAULT_SIZE, PageSizeFormatException::new);
        validatePageSize(size);

        var zeroBasedPage = PageUtils.zeroIndexPage(page);

        return PageRequest.of(zeroBasedPage, size);
    }

    private int parseIntegerParameter(String parameterName, int defaultValue, Function<String, RuntimeException> exceptionSupplier) {
        var value = paginationParams.get(parameterName);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw exceptionSupplier.apply(parameterName);
        }
    }

    private void validatePageSize(int size) {
        if (size < 1 || size > MAX_SIZE) {
            throw new PageSizeFormatException(SIZE_PARAM);
        }
    }

    private void validatePageNumber(int page) {
        if (page < 1) {
            throw new PageNumberFormatException(PAGE_PARAM);
        }
    }

}
