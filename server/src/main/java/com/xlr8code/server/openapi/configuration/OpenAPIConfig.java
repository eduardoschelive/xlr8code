package com.xlr8code.server.swagger.configuration;

import com.xlr8code.server.filter.enums.FilterOperation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static com.xlr8code.server.openapi.utils.OpenAPIUtils.FILTER_DOC_ANCHOR;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("xlr8code")
                        .description(buildDescription())
                        .version("v0.0.1"));
    }

    private String buildDescription() {
        var operations = FilterOperation.values();
        var descriptionMarkdown = new StringBuilder();

        descriptionMarkdown.append("## Introduction.\n\n")
                .append("This is the API for the website [xlr8code](https://xlr8code.com)\n\n")
                .append("## How to properly filter data on the API <div id=\"").append(FILTER_DOC_ANCHOR).append("\"></div>\n\n")
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
                .append("| Modifier | Description |\n")
                .append("| --- | --- |\n")
                .append("| `i` | Case-insensitive comparison **on strings** |\n")
                .append("| `n` | Negates the operation |\n");

        descriptionMarkdown.append("\n### Examples\n\n")
                .append("1. To filter all entities where the `name` field is equal to `John`, use the following query parameter:\n")
                .append("`name_eq=John`\n\n")
                .append("2. To filter all entities where the `age` field is greater than `18`, use the following query parameter:\n")
                .append("`age_gt=18`\n\n")
                .append("3. To filter all entities where the `name` field is not equal to `John`, use the following query parameter:\n")
                .append("`name_n-eq=John`\n\n")
                .append("4. To filter all entities where the `name` field is not equal to `John` in a case-insensitive manner, use the following query parameter:\n")
                .append("`name_n-eq-i=John`\n\n");

        descriptionMarkdown.append("\n\n");

        return descriptionMarkdown.toString();
    }
}
