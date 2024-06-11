package com.xlr8code.server.search.strategies;

import java.util.Arrays;

public interface ParsingStrategy {

    Object parse(String value);

    default Object[] parse(String[] values) {
        return Arrays.stream(values)
                .map(this::parse)
                .toArray();
    }

}
