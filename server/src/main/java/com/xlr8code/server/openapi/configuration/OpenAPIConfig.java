package com.xlr8code.server.openapi.configuration;

import com.xlr8code.server.filter.enums.FilterOperation;
import com.xlr8code.server.filter.utils.FilterConstants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static com.xlr8code.server.authentication.utils.SessionCookieUtils.SESSION_TOKEN_COOKIE_NAME;

@Configuration
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = SESSION_TOKEN_COOKIE_NAME, in = SecuritySchemeIn.COOKIE)
public class OpenAPIConfig {

    @Bean
    public OpenAPI xlr8codeOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("xlr8code")
                        .description(buildDescription())
                        .version("v0.0.1"));
    }

    private String buildDescription() {
        var operations = FilterOperation.values();
        var descriptionMarkdown = new StringBuilder();

        descriptionMarkdown.append("# Introduction\n\n")
                .append("This is the API for the website [xlr8code](https://xlr8code.com)\n\n")
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
                .append("| `").append(FilterConstants.CASE_INSENSITIVE_SUFFIX).append("` | Makes the operation case-insensitive | ").append("OPERATION SUFFIX | \n")
                .append("| `").append(FilterConstants.NEGATION_PREFIX).append("` | Negates the operation | ").append("OPERATION SUFFIX | \n\n");

        descriptionMarkdown.append("\n### Examples\n\n")
                .append("1. To filter all entities where the `name` field is equal to `John`, use the following query parameter:\n")
                .append("`name_eq=John`\n\n")
                .append("2. To filter all entities where the `age` field is greater than `18`, use the following query parameter:\n")
                .append("`age_gt=18`\n\n")
                .append("3. To filter all entities where the `name` field is not equal to `John`, use the following query parameter:\n")
                .append("`name").append(FilterConstants.NEGATION_PREFIX).append("eq=John`\n\n")
                .append("4. To filter all entities where the `name` field is not equal to `John` in a case-insensitive manner, use the following query parameter:\n")
                .append("`name").append(FilterConstants.NEGATION_PREFIX).append("eq").append(FilterConstants.CASE_INSENSITIVE_SUFFIX).append("=John`\n\n");

        descriptionMarkdown.append("\n\n");

        return descriptionMarkdown.toString();
    }

}
