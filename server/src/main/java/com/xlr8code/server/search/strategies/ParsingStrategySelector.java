package com.xlr8code.server.search.strategies;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodType;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingStrategySelector {

    private static final Map<Class<?>, ParsingStrategy<?>> STRATEGIES = Map.of(
            String.class, new StringParsing(),
            Boolean.class, new BooleanParsing()
    );

    public static Object parse(Class<?> expectedType, String value) {
        var wrapper = getWrapper(expectedType);
        ParsingStrategy<?> strategy = STRATEGIES.get(wrapper);

        if (strategy == null) {
            throw new IllegalArgumentException("No parsing strategy for type: " + expectedType);
        }

        return strategy.parse(value);
    }

    public static Class<?> getWrapper(Class<?> expectedType) {
        return MethodType.methodType(expectedType).wrap().returnType();
    }

}
