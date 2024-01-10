package com.xlr8code.server.common.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xlr8code.server.common.deserializers.LanguageDeserializer;
import com.xlr8code.server.common.deserializers.ThemeDeserializer;
import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module themeModule() {
        var module = new SimpleModule();
        module.addDeserializer(Theme.class, new ThemeDeserializer());
        return module;
    }

    @Bean
    public Module languageModule() {
        var module = new SimpleModule();
        module.addDeserializer(Language.class, new LanguageDeserializer());
        return module;
    }

}
