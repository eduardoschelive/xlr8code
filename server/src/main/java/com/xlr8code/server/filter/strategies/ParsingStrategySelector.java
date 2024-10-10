package com.xlr8code.server.filter.strategies;


import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.enums.Theme;
import com.xlr8code.server.common.utils.ClassUtils;
import com.xlr8code.server.filter.exception.NoStrategyDefinedException;
import com.xlr8code.server.user.utils.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodType;
import java.time.Instant;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingStrategySelector {

    private static final Map<Class<?>, ParsingStrategy> STRATEGIES = Map.of(
            String.class, new StringParsingStrategy(),
            Boolean.class, new BooleanParsingStrategy(),
            Theme.class, new EnumParsingStrategy<>(Theme::fromCode),
            Language.class, new EnumParsingStrategy<>(Language::fromCode),
            UserRole.class, new EnumParsingStrategy<>(UserRole::valueOf),
            Instant.class, new InstantParsingStrategy()
    );

    /**
     * @param expectedType the expected field type
     * @return the parsing strategy for the expected type
     */
    public static ParsingStrategy getStrategy(Class<?> expectedType) {
        var wrapper = ClassUtils.getWrapper(expectedType);
        var strategy = STRATEGIES.get(wrapper);

        if (strategy == null) {
            throw new NoStrategyDefinedException(wrapper.getSimpleName());
        }

        return strategy;
    }

}
