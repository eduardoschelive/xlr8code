package com.xlr8code.server.search.model;

import com.xlr8code.server.search.enums.SearchPagination;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class PaginationParams {

    private static final Map<String, SearchPagination> PARAM_ENUM = Arrays.stream(SearchPagination.values())
            .collect(Collectors.toMap(SearchPagination::getSuffix, Function.identity()));
    private int page = 0;
    private int size = 10;

    public static boolean isParamSupported(String param) {
        return PARAM_ENUM.containsKey(param.toLowerCase());
    }

    public void setParam(String param, String value) {
        var searchPagination = PARAM_ENUM.get(param.toLowerCase());

        if (searchPagination == null) {
            throw new IllegalArgumentException("No such SearchPagination with suffix: " + param + ". Valid suffixes are: " + PARAM_ENUM.keySet());
        }

        switch (searchPagination) {
            case PAGE:
                setPage(Integer.parseInt(value));
                break;
            case SIZE:
                setSize(Integer.parseInt(value));
                break;
            default:
                throw new IllegalArgumentException("Invalid SearchPagination type: " + searchPagination);
        }
    }

}