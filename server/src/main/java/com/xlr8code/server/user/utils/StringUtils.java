package com.xlr8code.server.user.utils;

public class StringUtils {

    public static String splitFromPascalCase(String string) {
        return string.replaceAll("(.)(\\p{Upper})", "$1 $2");
    }

}
