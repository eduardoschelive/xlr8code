package com.xlr8code.server.common.config;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xlr8code.server.common.serialization.deserializer.LanguageDeserializer;
import com.xlr8code.server.common.serialization.deserializer.ThemeDeserializer;
import com.xlr8code.server.common.serialization.serializer.LanguageSerializer;
import com.xlr8code.server.common.serialization.serializer.ThemeSerializer;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private <T> Module createSimpleModule(Class<T> clazz, JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
        var module = new SimpleModule();
        module.addSerializer(clazz, serializer);
        module.addDeserializer(clazz, deserializer);
        return module;
    }

    @Bean
    public Module themeModule() {
        return this.createSimpleModule(Theme.class, new ThemeSerializer(), new ThemeDeserializer());
    }

    @Bean
    public Module languageModule() {
        return this.createSimpleModule(Language.class, new LanguageSerializer(), new LanguageDeserializer());
    }
}
