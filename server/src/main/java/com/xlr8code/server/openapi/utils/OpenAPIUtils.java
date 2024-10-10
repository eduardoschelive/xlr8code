package com.xlr8code.server.openapi.utils;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.common.utils.ClassUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Parameter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAPIUtils {

    public static ApplicationException getExceptionMock(Class<? extends ApplicationException> exception) {
        try {
            var constructor = exception.getDeclaredConstructors()[0];
            var arguments = constructor.getParameters();
            var args = getArgs(arguments);
            return exception.cast(constructor.newInstance(args));
        } catch (Exception e) {
            var constructor = exception.getDeclaredConstructors()[0];
            var arguments = constructor.getParameters();
            var args = getArgs(arguments);

            System.out.println(args);
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
        var wrapper = ClassUtils.getWrapper(argument.getType());
        return switch (wrapper.getName()) {
            case "java.lang.String" -> argument.getName();
            case "java.lang.Integer" -> "{{Integer value}}";
            default -> null;
        };
    }

}
