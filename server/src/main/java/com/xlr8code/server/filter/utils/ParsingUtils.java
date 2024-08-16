package com.xlr8code.server.filter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingUtils {

    /**
     * @param input the string to parse
     * @return a list of strings
     */
    public static List<String> parseStringList(String input) {
        return List.of(input.split(","));
    }

}
