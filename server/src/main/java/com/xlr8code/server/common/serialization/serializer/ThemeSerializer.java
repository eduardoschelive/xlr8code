package com.xlr8code.server.common.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xlr8code.server.common.enums.Theme;

import java.io.IOException;

public class ThemeSerializer extends JsonSerializer<Theme> {
    @Override
    public void serialize(Theme value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getCode());
    }
}
