package com.xlr8code.server.filter.strategies;


import com.xlr8code.server.filter.exception.NoStrategyDefinedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingStrategySelector {

    // TODO: Not all classes will have a wrapper, change later
    private static Class<?> getWrapper(Class<?> expectedType) {
        return MethodType.methodType(expectedType).wrap().returnType();
    }

    public static ParsingStrategy getStrategy(Class<?> expectedType) {
        var wrapper = getWrapper(expectedType);

        if (wrapper == String.class) {
            return new StringParsingStrategy();
        }

        if (wrapper == Boolean.class) {
            return new BooleanParsingStrategy();
        }

        throw new NoStrategyDefinedException(wrapper.getSimpleName());
    }

}
