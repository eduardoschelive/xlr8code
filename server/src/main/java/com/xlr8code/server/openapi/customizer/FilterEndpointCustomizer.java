package com.xlr8code.server.openapi.customizer;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.strategies.ParsingStrategySelector;
import com.xlr8code.server.filter.utils.FilterFieldDetails;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.openapi.annotation.FilterEndpoint;
import com.xlr8code.server.openapi.helper.OpenAPIExceptionHelper;
import com.xlr8code.server.openapi.utils.FilterExceptionsUtils;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilterEndpointCustomizer implements OperationCustomizer {

    private final OpenAPIExceptionHelper openAPIExceptionHelper;

    private static BiConsumer<String, FilterFieldDetails> buildFieldDetails(StringBuilder descriptionMarkdown) {
        return (field, details) -> {
            var strategy = ParsingStrategySelector.getStrategy(details.fieldType());
            String supportedOperations = strategy.getSupportedFilterOperations().stream()
                    .map(FilterOperation::getSuffix)
                    .collect(Collectors.joining(", "));

            descriptionMarkdown.append("| ")
                    .append(field).append(" | ")
                    .append(supportedOperations).append(" | ")
                    .append(details.fieldType().getSimpleName()).append(" |\n");
        };
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var annotation = handlerMethod.getMethodAnnotation(FilterEndpoint.class);
        if (annotation != null) {
            addFilterExceptions(operation);
            addFilterDetails(operation, annotation);
        }
        return operation;
    }

    private void addFilterExceptions(Operation operation) {
        var exceptions = FilterExceptionsUtils.getAllFilterExceptions();
        var groupedExceptions = openAPIExceptionHelper.groupExceptionsByStatusCode(exceptions);
        groupedExceptions.forEach((httpStatus, applicationExceptions) ->
                openAPIExceptionHelper.addExceptionResponse(operation, httpStatus, applicationExceptions));
    }

    private void addFilterDetails(Operation operation, FilterEndpoint annotation) {
        var filterClass = annotation.value();
        var firstParam = operation.getParameters().getFirst();
        var filterFields = FilterUtils.extractFilterableFields(filterClass);

        configureFirstParameter(firstParam);
        firstParam.setDescription(generateDescription(filterFields));
    }

    private void configureFirstParameter(Parameter firstParam) {
        firstParam.setName("queryString");
        firstParam.setExplode(true);
        firstParam.setStyle(Parameter.StyleEnum.FORM);
        firstParam.setIn(ParameterIn.QUERY.toString());
        firstParam.setRequired(false);
        firstParam.addExample("field_operation=value", new Example().value(Map.of(
                "field_operation", "value"
        )));
    }

    private String generateDescription(Map<String, FilterFieldDetails> filterFields) {
        var descriptionMarkdown = new StringBuilder();

        descriptionMarkdown.append("\n### Filterable fields\n\n")
                .append("The following fields can be used to filter the data.\n\n")
                .append("| Field | Available operations | Type |\n")
                .append("| --- | --- | --- |\n");

        filterFields.forEach(buildFieldDetails(descriptionMarkdown));

        return descriptionMarkdown.toString();
    }
}
