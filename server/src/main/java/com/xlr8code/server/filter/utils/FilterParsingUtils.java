package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.exception.InvalidInstantFormatException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.List;

import static com.xlr8code.server.filter.utils.FilterConstants.FILTER_VALUE_SEPARATOR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterParsingUtils {

    /**
     * @param input the string to parse
     * @return the parsed filter values
     */
    public static String[] parseFilterValues(String input) {
        return input.split(FILTER_VALUE_SEPARATOR);
    }

    /**
     * @param input the string to parse
     * @return the parsed instant
     */
    public static Instant parseInstant(String input) {
        try {
            return Instant.parse(input);
        } catch (DateTimeException e) {
            throw new InvalidInstantFormatException(input);
        }
    }

}
