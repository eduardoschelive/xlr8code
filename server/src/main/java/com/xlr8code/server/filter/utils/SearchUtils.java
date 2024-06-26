package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.annotation.NestedSearchable;
import com.xlr8code.server.filter.annotation.Searchable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtils {

    public static Map<String, FilterableFieldDetails> extractSearchableFields(Class<?> targetClass) {
        Map<String, FilterableFieldDetails> searchableFields = new HashMap<>();
        var fieldPathStack = new ArrayDeque<String>();
        traverseFields(fieldPathStack, targetClass, searchableFields);
        return searchableFields;
    }

    private static void traverseFields(Deque<String> fieldPathStack, Class<?> currentClass, Map<String, FilterableFieldDetails> searchableFields) {
        for (var field : currentClass.getDeclaredFields()) {
            evaluateField(field, fieldPathStack, searchableFields);
        }
    }

    private static void evaluateField(Field field, Deque<String> fieldPathStack, Map<String, FilterableFieldDetails> searchableFields) {
        if (field.isAnnotationPresent(Searchable.class)) {
            var annotation = field.getAnnotation(Searchable.class);
            var fullPath = constructFieldPath(fieldPathStack, field.getName());
            var customPath = annotation.customPath().isBlank() ? fullPath : annotation.customPath();
            searchableFields.put(customPath, new FilterableFieldDetails(field.getType(), fullPath));
        }

        if (field.isAnnotationPresent(NestedSearchable.class)) {
            fieldPathStack.addLast(field.getName());
            traverseFields(fieldPathStack, getGenericType(field), searchableFields);
            fieldPathStack.removeLast();
        }
    }

    private static String constructFieldPath(Deque<String> fieldPathStack, String fieldName) {
        return String.join(".", fieldPathStack) + (fieldPathStack.isEmpty() ? "" : ".") + fieldName;
    }

    private static Class<?> getGenericType(Field field) {
        var type = field.getGenericType();
        if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        return (Class<?>) type;
    }

}
