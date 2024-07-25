package com.xlr8code.server.openapi.configuration;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.utils.FilterConstants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static com.xlr8code.server.authentication.utils.SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME;
import static com.xlr8code.server.filter.utils.FilterConstants.*;

@Configuration
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = SESSION_TOKEN_COOKIE_NAME, in = SecuritySchemeIn.COOKIE)
public class OpenAPIConfig {

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.url}")
    private String applicationUrl;

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info().title(this.applicationName)
                        .description(buildDescription())
                        .version("v0.0.1"));
    }

    private String buildDescription() {
        var operations = FilterOperation.values();
        var descriptionMarkdown = new StringBuilder();

        descriptionMarkdown.append("# Introduction\n\n")
                .append("This is the API for the website [").append(this.applicationName).append("](").append(this.applicationUrl).append(")").append("\n\n")
                .append("# How to properly filter data on the API endpoints\n\n")
                .append("This API allows you to filter and paginated data by passing query parameters to the endpoints with a specific format.\n\n")
                .append("### Syntax\n\n")
                .append("The filter parameters are passed as query parameters in the following format:\n\n")
                .append("`field_operation=value`\n\n")
                .append("Where:\n\n")
                .append("- `field` is the field to filter by\n")
                .append("- `operation` is the operation to apply to the field\n")
                .append("- `value` is the value to filter by\n\n")
                .append("### Filter Operations\n\n")
                .append("| Operation | Description |\n")
                .append("| --- | --- |\n");

        Arrays.stream(operations)
                .forEach(filterOperation -> descriptionMarkdown.append("| ").append(filterOperation.getSuffix()).append(" | ").append(filterOperation).append(" |\n"));

        descriptionMarkdown.append("\n### Modifiers\n\n")
                .append("Some operations have modifiers that change the behavior of the operation.\n\n")
                .append("| Modifier | Description | Type | \n")
                .append("| --- | --- | --- |\n")
                .append("| `").append(CASE_INSENSITIVE_SUFFIX).append("` | Makes the operation case-insensitive | ").append("OPERATION SUFFIX | \n")
                .append("| `").append(NEGATION_PREFIX).append("` | Negates the operation | ").append("OPERATION SUFFIX | \n\n");

        descriptionMarkdown.append("\n### Examples\n\n")
                .append("1. To filter all entities where the `name` field is equal to `John`, use the following query parameter:\n")
                .append("`name").append(FILTER_PARAM_SEPARATOR).append("eq=John`\n\n")
                .append("2. To filter all entities where the `age` field is greater than `18`, use the following query parameter:\n")
                .append("`age").append(FILTER_PARAM_SEPARATOR).append("gt=18`\n\n")
                .append("3. To filter all entities where the `name` field is not equal to `John`, use the following query parameter:\n")
                .append("`name").append(FILTER_PARAM_SEPARATOR).append(NEGATION_PREFIX).append("eq=John`\n\n")
                .append("4. To filter all entities where the `name` field is not equal to `John` in a case-insensitive manner, use the following query parameter:\n")
                .append("`name").append(NEGATION_PREFIX).append("eq").append(CASE_INSENSITIVE_SUFFIX).append("=John`\n\n");

        descriptionMarkdown.append("\n### Sorting\n\n")
                .append("The sorting order for the data. The format is `field_").append(SORT_PARAM).append("=direction`.\n\n")
                .append("The available directions are: ")
                .append(String.join(", ", ACCEPTED_SORT_VALUES))
                .append(".\n\n");

        descriptionMarkdown.append("Examples:\n\n")
                .append("1. To sort the data by the `name` field in ascending order, use the following query parameter:\n")
                .append("`name").append(FILTER_PARAM_SEPARATOR).append("asc`\n\n")
                .append("2. To sort the data by the `name` field in descending order, use the following query parameter:\n")
                .append("`name").append(FILTER_PARAM_SEPARATOR).append("desc`\n\n");

        descriptionMarkdown.append("\n### Pagination\n\n")
                .append("The following query parameters can be used to paginate the data:\n\n")
                .append("|  Operation | Description |\n")
                .append("| --- | --- |\n")
                .append("| `").append(PAGE_PARAM).append("` | The page number to retrieve. Default is 1. |\n")
                .append("| `").append(SIZE_PARAM).append("` | The number of elements to retrieve. Default is").append(DEFAULT_SIZE).append(". Maximum is ").append(MAX_SIZE).append(". |\n\n")
                .append("Examples:\n\n")
                .append("1. To get the data from page 2, use the following query parameter:\n")
                .append("`").append(PAGE_PARAM).append("=2`\n\n")
                .append("2. To change the size of the page to 100, use the following query parameter:\n")
                .append("`").append(SIZE_PARAM).append("=100`\n\n");

        descriptionMarkdown.append("\n\n");

        return descriptionMarkdown.toString();
    }

}
