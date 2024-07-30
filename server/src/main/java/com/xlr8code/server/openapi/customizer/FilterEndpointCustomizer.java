package com.xlr8code.server.openapi.customizer;

import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.strategies.ParsingStrategySelector;
import com.xlr8code.server.filter.utils.FilterFieldDetails;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.openapi.helper.OpenAPIExceptionHelper;
import com.xlr8code.server.openapi.utils.FilterExceptionsUtils;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.xlr8code.server.filter.utils.FilterConstants.*;

@Component
@RequiredArgsConstructor
public class FilterEndpointCustomizer implements OperationCustomizer {

    private final OpenAPIExceptionHelper openAPIExceptionHelper;

    private static BiConsumer<String, FilterFieldDetails> filterDescriptionConsumer(StringBuilder descriptionMarkdown) {
        return (field, details) -> {
            var strategy = ParsingStrategySelector.getStrategy(details.fieldType());
            var supportedOperations = strategy.getSupportedFilterOperations().stream()
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
            operation.setParameters(null);
            addFilterDetails(operation, annotation);
            addPageableDetails(operation, annotation);
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
        var filterFields = FilterUtils.extractFilterableFields(filterClass);

        var filterParam = new Parameter()
                .name("query")
                .explode(true)
                .style(Parameter.StyleEnum.FORM)
                .in(ParameterIn.QUERY.toString())
                .required(false)
                .addExample("field_operation=value", new Example().value(Map.of("field_operation", "value")))
                .description(generateDescription(filterFields))
                .schema(new MapSchema());

        operation.addParametersItem(filterParam);
    }

    private void addPageableDetails(Operation operation, FilterEndpoint annotation) {
        var filterParam = new Parameter()
                .name(PAGE_PARAM)
                .in(ParameterIn.QUERY.toString())
                .required(false)
                .addExample("Getting data from page 2", new Example().value(Map.of(PAGE_PARAM, "2")))
                .description("The page number to retrieve. Default is " + DEFAULT_PAGE + ".")
                .schema(new IntegerSchema());

        var sizeParam = new Parameter()
                .name(SIZE_PARAM)
                .in(ParameterIn.QUERY.toString())
                .required(false)
                .addExample("Changing the size of the page to 100", new Example().value(Map.of(SIZE_PARAM, "100")))
                .description("The number of elements to retrieve. Default is " + DEFAULT_SIZE + ". Maximum is " + MAX_SIZE + ".")
                .schema(new IntegerSchema());


        var sortParam = new Parameter()
                .name(SORT_PARAM)
                .in(ParameterIn.QUERY.toString())
                .required(false)
                .addExample("Sorting a field by asc order", new Example().value(Map.of("field_sort", "asc")))
                .description(generateSortDescription(annotation))
                .explode(true)
                .style(Parameter.StyleEnum.FORM)
                .schema(new MapSchema());

        operation.addParametersItem(sortParam);
        operation.addParametersItem(sizeParam);
        operation.addParametersItem(filterParam);
    }

    private String generateDescription(Map<String, FilterFieldDetails> filterFields) {
        var descriptionMarkdown = new StringBuilder();
        descriptionMarkdown.append("\n### Filterable fields\n\n")
                .append("The following fields can be used to filter the data.\n\n")
                .append("| Field | Available operations | Type |\n")
                .append("| --- | --- | --- |\n");

        filterFields.forEach(filterDescriptionConsumer(descriptionMarkdown));
        return descriptionMarkdown.toString();
    }

    private String generateSortDescription(FilterEndpoint annotation) {
        var filterFields = FilterUtils.extractFilterableFields(annotation.value());
        var sortDescription = new StringBuilder()
                .append("The sorting order for the data. The format is `field_sort=direction`.\n\n")
                .append("The available directions are: ")
                .append(String.join(", ", ACCEPTED_SORT_VALUES))
                .append(".\n\n")
                .append("The available fields are:\n\n")
                .append("| Field | Direction |\n")
                .append("| --- | --- |\n");

        filterFields.forEach((field, details) ->
                sortDescription.append("| ").append(field).append(" | asc, desc |\n"));
        return sortDescription.toString();
    }
}
