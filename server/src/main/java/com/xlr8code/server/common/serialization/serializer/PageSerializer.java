package com.xlr8code.server.common.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.io.IOException;

@JsonComponent
public class PageSerializer extends JsonSerializer<PageImpl<?>> {

    private static void writePaginationInfo(PageImpl<?> page, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeObjectFieldStart("pagination");

        jsonGenerator.writeNumberField("size", page.getSize());
        jsonGenerator.writeNumberField("number", page.getNumber());
        jsonGenerator.writeBooleanField("first", page.isFirst());
        jsonGenerator.writeBooleanField("last", page.isLast());
        jsonGenerator.writeBooleanField("empty", page.isEmpty());
        jsonGenerator.writeBooleanField("hasNext", page.hasNext());
        jsonGenerator.writeBooleanField("hasPrevious", page.hasPrevious());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeNumberField("numberOfElements", page.getNumberOfElements());

        jsonGenerator.writeEndObject();
    }

    private static void writeSortInfo(PageImpl<?> page, JsonGenerator jsonGenerator) throws IOException {
        var sort = page.getSort();

        jsonGenerator.writeArrayFieldStart("sort");

        for (Sort.Order order : sort) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("property", order.getProperty());
            jsonGenerator.writeStringField("direction", order.getDirection().name());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    @Override
    public void serialize(PageImpl page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("content", page.getContent());
        writePaginationInfo(page, jsonGenerator);
        writeSortInfo(page, jsonGenerator);

        jsonGenerator.writeEndObject();
    }
}