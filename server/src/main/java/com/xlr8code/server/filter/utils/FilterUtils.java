package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.annotation.NestedFilterable;
import com.xlr8code.server.filter.annotation.Filterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterUtils {

    public static Map<String, FilterableFieldDetails> extractFilterableFields(Class<?> targetClass) {
        Map<String, FilterableFieldDetails> fieldDetailsHashMap = new HashMap<>();
        var fieldPathStack = new ArrayDeque<String>();
        traverseFields(fieldPathStack, targetClass, fieldDetailsHashMap);
        return fieldDetailsHashMap;
    }

    private static void traverseFields(Deque<String> fieldPathStack, Class<?> currentClass, Map<String, FilterableFieldDetails> fieldDetailsHashMap) {
        for (var field : currentClass.getDeclaredFields()) {
            evaluateField(field, fieldPathStack, fieldDetailsHashMap);
        }
    }

    private static void evaluateField(Field field, Deque<String> fieldPathStack, Map<String, FilterableFieldDetails> fieldDetailsHashMap) {
        if (field.isAnnotationPresent(Filterable.class)) {
            var annotation = field.getAnnotation(Filterable.class);
            var fullPath = constructFieldPath(fieldPathStack, field.getName());
            var customPath = annotation.customPath().isBlank() ? fullPath : annotation.customPath();
            fieldDetailsHashMap.put(customPath, new FilterableFieldDetails(field.getType(), fullPath));
        }

        if (field.isAnnotationPresent(NestedFilterable.class)) {
            fieldPathStack.addLast(field.getName());
            traverseFields(fieldPathStack, getGenericType(field), fieldDetailsHashMap);
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
