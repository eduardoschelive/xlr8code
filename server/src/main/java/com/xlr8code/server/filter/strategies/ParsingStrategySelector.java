package com.xlr8code.server.filter.strategies;


import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.filter.exception.NoStrategyDefinedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodType;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingStrategySelector {

    private static Class<?> getWrapper(Class<?> expectedType) {
        return MethodType.methodType(expectedType).wrap().returnType();
    }

    private static final Map<Class<?>, ParsingStrategy> STRATEGIES = Map.of(
            String.class, new StringParsingStrategy(),
            Boolean.class, new BooleanParsingStrategy(),
            Theme.class, new EnumParsingStrategy<>(Theme::fromCode),
            Language.class, new EnumParsingStrategy<>(Language::fromCode)
    );

    public static ParsingStrategy getStrategy(Class<?> expectedType) {
        var wrapper = getWrapper(expectedType);
        var strategy = STRATEGIES.get(wrapper);

        if (strategy == null) {
            throw new NoStrategyDefinedException(wrapper.getSimpleName());
        }

        return strategy;
    }

}
