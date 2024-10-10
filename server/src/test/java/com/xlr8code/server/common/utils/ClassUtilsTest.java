package com.xlr8code.server.common.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClassUtilsTest {

    private static Stream<Arguments> providePrimitiveParameters() {
        return Stream.of(
                Arguments.of(int.class, Integer.class),
                Arguments.of(long.class, Long.class),
                Arguments.of(double.class, Double.class),
                Arguments.of(float.class, Float.class),
                Arguments.of(short.class, Short.class),
                Arguments.of(byte.class, Byte.class),
                Arguments.of(char.class, Character.class),
                Arguments.of(boolean.class, Boolean.class)
        );
    }

    @ParameterizedTest
    @MethodSource("providePrimitiveParameters")
    void it_should_get_the_proper_wrapper_class(Class<?> primitiveType, Class<?> expected) {
        var actual = ClassUtils.getWrapper(primitiveType);

        assertEquals(expected, actual);
    }
}