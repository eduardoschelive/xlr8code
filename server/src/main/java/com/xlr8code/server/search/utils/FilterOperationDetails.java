package com.xlr8code.server.search.utils;

import com.xlr8code.server.search.enums.FilterOperation;

public record FilterOperationDetails(
        FilterOperation filterOperation,
        boolean negated,
        boolean isCaseInsensitive
) {
}
