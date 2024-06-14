package com.xlr8code.server.search.model;

import com.xlr8code.server.search.enums.SearchOperation;
import com.xlr8code.server.search.strategies.ParsingStrategySelector;
import lombok.Getter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
public class FilterParams {

    private final Map<SearchOperation, List<Pair<String, Object>>> searchParams = new EnumMap<>(SearchOperation.class);

    private static final Map<String, SearchOperation> SUFFIX_ENUM = Arrays.stream(SearchOperation.values())
            .collect(Collectors.toMap(SearchOperation::getSuffix, Function.identity()));

    public static boolean isSupported(String suffix) {
        return SUFFIX_ENUM.containsKey(suffix.toLowerCase());
    }

    public void computeIfAbsent(String operation, String fieldPath, String value, Class<?> expectedType) {
        SearchOperation searchOperation = fromSuffix(operation);
        Object parsedValue = ParsingStrategySelector.parse(expectedType, value);
        Pair<String, Object> fieldPathPair = new Pair<>(fieldPath, parsedValue);

        searchParams.computeIfAbsent(searchOperation, k -> new ArrayList<>()).add(fieldPathPair);
    }

    private SearchOperation fromSuffix(String suffix) {
        SearchOperation result = SUFFIX_ENUM.get(suffix.toLowerCase());

        if (result == null) {
            throw new IllegalArgumentException("No such SearchOperation with suffix: " + suffix + ". Valid suffixes are: " + SUFFIX_ENUM.keySet());
        }

        return result;
    }
}
