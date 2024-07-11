package com.xlr8code.server.common.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xlr8code.server.common.utils.PageUtils;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.io.IOException;

@JsonComponent
@SuppressWarnings({"java:S3740", "rawtypes"} ) // disable for testing purposes
public class PageSerializer extends JsonSerializer<PageImpl> {

    @Override
    public void serialize(PageImpl page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("content", page.getContent());
        writePaginationInfo(page, jsonGenerator);
        writeSortInfo(page, jsonGenerator);

        jsonGenerator.writeEndObject();
    }

    private void writePaginationInfo(PageImpl<?> page, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeObjectFieldStart("pagination");
        writePageDetails(page, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    private void writePageDetails(PageImpl<?> page, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNumberField("pageSize", page.getSize());
        jsonGenerator.writeNumberField("pageNumber", PageUtils.oneIndexPage(page.getNumber()));
        jsonGenerator.writeBooleanField("first", page.isFirst());
        jsonGenerator.writeBooleanField("last", page.isLast());
        jsonGenerator.writeBooleanField("empty", page.isEmpty());
        jsonGenerator.writeBooleanField("hasNext", page.hasNext());
        jsonGenerator.writeBooleanField("hasPrevious", page.hasPrevious());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeNumberField("numberOfElements", page.getNumberOfElements());
    }

    private void writeSortInfo(PageImpl<?> page, JsonGenerator jsonGenerator) throws IOException {
        var sort = page.getSort();

        jsonGenerator.writeArrayFieldStart("sort");
        writeSortDetails(sort, jsonGenerator);
        jsonGenerator.writeEndArray();
    }

    private void writeSortDetails(Sort sort, JsonGenerator jsonGenerator) throws IOException {
        for (Sort.Order order : sort) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("property", order.getProperty());
            jsonGenerator.writeStringField("direction", order.getDirection().name());
            jsonGenerator.writeEndObject();
        }
    }
}