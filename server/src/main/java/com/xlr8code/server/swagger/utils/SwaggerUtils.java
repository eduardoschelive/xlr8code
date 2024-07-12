package com.xlr8code.server.swagger.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Parameter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwaggerUtils {

    public static ApplicationException getExceptionMock(Class<? extends ApplicationException> exception) {
        try {
            var constructor = exception.getDeclaredConstructor();
            var arguments = constructor.getParameters();
            var args = getArgs(arguments);
            return exception.getDeclaredConstructor().newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Could not create exception mock", e);
        }
    }

    public static Object[] getArgs(Parameter[] arguments) {
        if (arguments.length == 0) {
            return new Object[0];
        }

        var args = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = getMockArg(arguments[i]);
        }

        return args;
    }

    public static Object getMockArg(Parameter argument) {
        return switch (argument.getType().getName()) {
            case "java.lang.String" -> argument.getName();
            case "java.lang.Integer" -> 1;
            default -> null;
        };
    }

}
