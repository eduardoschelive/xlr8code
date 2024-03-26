package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    private static final String CAMEL_CASE_REGEX = "(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])";

    /**
     * @param string the string to be converted to a human-readable string (e.g. "camelCase")
     * @return the human-readable string corresponding to the given string (e.g. "camel Case")
     */
    public static String splitPascalCase(String string) {
        return string.replaceAll(CAMEL_CASE_REGEX, " ");
    }

    /**
     * @param s the string to be checked
     * @return true if the given string is null or blank, false otherwise
     * @see String#isBlank()
     */
    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
