package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.enums.FilterOperation;

public record FilterOperationDetails(
        FilterOperation filterOperation,
        boolean negated,
        boolean isCaseInsensitive
) {
}
