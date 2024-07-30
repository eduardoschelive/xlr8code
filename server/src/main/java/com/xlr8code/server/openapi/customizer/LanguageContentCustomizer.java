package com.xlr8code.server.openapi.customizer;

import com.xlr8code.server.common.enums.Language;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class LanguageContentCustomizer implements PropertyCustomizer {

    @Override
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public Schema customize(Schema property, AnnotatedType type) {
        if (isLanguageContent(type)) {
            var oldSchema = (Schema<?>) property.getAdditionalProperties();
            var ref = oldSchema.get$ref();
            property.additionalProperties(false);
            property.properties(createLanguagePropertiesMap(ref));
            property.maxItems(Language.values().length);
            property.minItems(1);
            property.uniqueItems(true);
        }
        return property;
    }

    private Map<String, Schema<?>> createLanguagePropertiesMap(String ref) {
        return Arrays.stream(Language.values())
                .collect(HashMap::new, (map, language) -> map.put(language.getCode(), new Schema<>().$ref(ref)), Map::putAll);
    }

    // Since java has type erasure, we need to check the type name to determine if it is a Map<Language, T>
    private boolean isLanguageContent(AnnotatedType type) {
        String typeName = type.getType().getTypeName();
        return typeName.contains(Map.class.getName()) && typeName.contains(Language.class.getName());
    }
}