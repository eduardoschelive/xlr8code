package com.xlr8code.server.filter.strategies;

import com.xlr8code.server.filter.exception.NoStrategyDefinedException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ParsingStrategySelectorTest {

    @Test
    void it_should_get_parsing_strategy() {
        var parsingStrategy = ParsingStrategySelector.getStrategy(String.class);
        assertNotNull(parsingStrategy);
    }

    @Test
    void it_should_get_parsing_strategy_for_primitives() {
        var parsingStrategy = ParsingStrategySelector.getStrategy(boolean.class);
        assertNotNull(parsingStrategy);
    }

    @Test
    void it_should_throw_exception_when_no_strategy_defined() {
        assertThrows(NoStrategyDefinedException.class, () -> ParsingStrategySelector.getStrategy(Object.class));
    }

}