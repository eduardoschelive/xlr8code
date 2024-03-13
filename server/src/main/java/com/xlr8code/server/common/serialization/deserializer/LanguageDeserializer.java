package com.xlr8code.server.common.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.xlr8code.server.common.enums.Language;

import java.io.IOException;

public class LanguageDeserializer extends JsonDeserializer<Language> {
    @Override
    public Language deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var language = jsonParser.readValueAs(String.class);
        return Language.fromCode(language);
    }
}
