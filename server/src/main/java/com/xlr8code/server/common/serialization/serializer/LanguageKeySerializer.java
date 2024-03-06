package com.xlr8code.server.common.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xlr8code.server.common.utils.Language;

import java.io.IOException;

public class LanguageKeySerializer extends JsonSerializer<Language> {
    @Override
    public void serialize(Language value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(value.getCode());
    }
}
