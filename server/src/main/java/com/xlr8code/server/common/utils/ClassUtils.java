package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtils {

    /**
     * @param expectedType the expected field type
     * @return the wrapper class for the expected type
     */
    public static Class<?> getWrapper(Class<?> expectedType) {
        return MethodType.methodType(expectedType).wrap().returnType();
    }

}
