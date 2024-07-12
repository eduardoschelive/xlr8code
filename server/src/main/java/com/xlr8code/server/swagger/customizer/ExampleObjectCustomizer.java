package com.xlr8code.server.swagger.customizer;

import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.swagger.annotation.ErrorResponse;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Parameter;

@Component
public class ExampleObjectCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var annotation = handlerMethod.getMethodAnnotation(ErrorResponse.class);
        if (annotation != null) {
            var exception = getExceptionMock(annotation.exception());
            addErrorAnnotations(operation, exception);
        }

        return operation;
    }

    private ApplicationException getExceptionMock(Class<? extends ApplicationException> exception) {
        try {
            var constructor = exception.getDeclaredConstructor();
            var arguments = constructor.getParameters();
            var args = getArgs(arguments);
            return exception.getDeclaredConstructor().newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Could not create exception mock", e);
        }
    }

    private Object[] getArgs(Parameter[] arguments) {
        if (arguments.length == 0) {
            return new Object[0];
        }

        var args = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = getMockArg(arguments[i]);
        }

        return args;
    }

    private Object getMockArg(Parameter argument) {
        return switch (argument.getType().getName()) {
            case "java.lang.String" -> "example value";
            case "java.lang.Integer" -> 1;
            case "java.lang.Long" -> 1L;
            case "java.lang.Double" -> 1.0;
            case "java.lang.Float" -> 1.0f;
            case "java.lang.Boolean" -> true;
            default -> null;
        };
    }

    private void addErrorAnnotations(Operation operation, ApplicationException exception) {
        System.out.println(exception.getHttpStatus());
        System.out.println(exception.getMessageIdentifier());
        System.out.println(exception.getMessage());
    }

}
