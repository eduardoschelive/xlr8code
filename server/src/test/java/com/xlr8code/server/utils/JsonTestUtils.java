package com.xlr8code.server.utils;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xlr8code.server.common.serialization.deserializer.LanguageDeserializer;
import com.xlr8code.server.common.serialization.deserializer.ThemeDeserializer;
import com.xlr8code.server.common.serialization.serializer.LanguageKeySerializer;
import com.xlr8code.server.common.serialization.serializer.LanguageSerializer;
import com.xlr8code.server.common.serialization.serializer.ThemeSerializer;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;

public class JsonTestUtils {

    private static <T> Module createSimpleModule(Class<T> clazz, JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
        var module = new SimpleModule();
        module.addSerializer(clazz, serializer);
        module.addDeserializer(clazz, deserializer);
        return module;
    }

    private static <T> Module createSimpleKeyModule(Class<T> clazz, JsonSerializer<T> keySerializer) {
        var module = new SimpleModule();
        module.addKeySerializer(clazz, keySerializer);
        return module;
    }

    private static <T> void registerNewModule(ObjectMapper objectMapper, Class<T> clazz, JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
        objectMapper.registerModule(createSimpleModule(clazz, serializer, deserializer));
    }

    private static <T> void registerNewKeyModule(ObjectMapper objectMapper, Class<T> clazz, JsonSerializer<T> keySerializer) {
        objectMapper.registerModule(createSimpleKeyModule(clazz, keySerializer));
    }

    private static void registerModules(ObjectMapper objectMapper) {
        registerNewModule(objectMapper, Theme.class, new ThemeSerializer(), new ThemeDeserializer());
        registerNewModule(objectMapper, Language.class, new LanguageSerializer(), new LanguageDeserializer());
        registerNewKeyModule(objectMapper, Language.class, new LanguageKeySerializer());
    }

    private static ObjectMapper getObjectMapper() {
        var objectMapper = new ObjectMapper();

        registerModules(objectMapper);

        return objectMapper;
    }

    public static String asJsonString(final Object obj) {
        var objectMapper = getObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
