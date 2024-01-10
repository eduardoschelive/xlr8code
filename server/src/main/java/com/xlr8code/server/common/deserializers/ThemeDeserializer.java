package com.xlr8code.server.common.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.xlr8code.server.common.utils.Theme;

import java.io.IOException;

public class ThemeSerializer extends JsonDeserializer<Theme> {
    @Override
    public Theme deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        var theme = jsonParser.readValueAs(String.class);

        return Theme.fromCode(theme);

    }
}
