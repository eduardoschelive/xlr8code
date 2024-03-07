package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleUtils {

    public static Double tryParse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double tryParse(String value, Double defaultValue) {
        Double parsedValue = tryParse(value);
        return parsedValue != null ? parsedValue : defaultValue;
    }

}
