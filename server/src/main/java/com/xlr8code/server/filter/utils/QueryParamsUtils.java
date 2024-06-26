package com.xlr8code.server.filter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParamsUtils {

    public static void removePageableParams(Map<String, String> queryParameters) {
        queryParameters.remove("page");
        queryParameters.remove("size");
        queryParameters.remove("sort");
    }

}
