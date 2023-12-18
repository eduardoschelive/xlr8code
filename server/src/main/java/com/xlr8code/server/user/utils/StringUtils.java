package com.xlr8code.server.user.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String splitFromPascalCase(String string) {
        return string.replaceAll("(.)(\\p{Upper})", "$1 $2");
    }

}
