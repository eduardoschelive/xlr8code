package com.xlr8code.server.search.strategies;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingStrategySelector {

    // TODO: Not all classes will have a wrapper, change later
    private static  Class<?> getWrapper(Class<?> expectedType) {
        return MethodType.methodType(expectedType).wrap().returnType();
    }

    public static ParsingStrategy getStrategy(Class<?> expectedType) {
        var wrapper = getWrapper(expectedType);

        if (wrapper == String.class) {
            return new StringParsing();
        }

        throw new IllegalArgumentException("Unsupported type: " + expectedType);
    }

}
