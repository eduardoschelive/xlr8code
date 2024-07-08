package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.annotation.Filterable;
import com.xlr8code.server.filter.annotation.NestedFilterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static com.xlr8code.server.filter.utils.FilterConstants.SEARCH_PARAM_SEPARATOR;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterUtils {

    /**
     * @param targetClass the class to extract the filterable fields from
     * @return a map of filterable fields and their details
     */
    public static Map<String, FilterFieldDetails> extractFilterableFields(Class<?> targetClass) {
        Map<String, FilterFieldDetails> fieldDetailsHashMap = new HashMap<>();
        var fieldPathStack = new ArrayDeque<String>();
        traverseFields(fieldPathStack, targetClass, fieldDetailsHashMap);
        return fieldDetailsHashMap;
    }

    private static void traverseFields(Deque<String> fieldPathStack, Class<?> currentClass, Map<String, FilterFieldDetails> fieldDetailsHashMap) {
        for (var field : currentClass.getDeclaredFields()) {
            evaluateField(field, fieldPathStack, fieldDetailsHashMap);
        }
    }

    private static void evaluateField(Field field, Deque<String> fieldPathStack, Map<String, FilterFieldDetails> fieldDetailsHashMap) {
        if (field.isAnnotationPresent(Filterable.class)) {
            var annotation = field.getAnnotation(Filterable.class);
            var fullPath = constructFieldPath(fieldPathStack, field.getName());
            var customPath = annotation.customPath().isBlank() ? fullPath : annotation.customPath();
            fieldDetailsHashMap.put(customPath, new FilterFieldDetails(field.getType(), fullPath));
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

    /**
     * @param key the key to extract the field path from
     * @return the field path extracted from the key
     */
    public static String extractFieldPath(String key) {
        int separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(0, separatorIndex).trim() : null;
    }

    /**
     * @param key the key to extract the operation from
     * @return the operation extracted from the key
     */
    public static String extractOperation(String key) {
        int separatorIndex = key.lastIndexOf(SEARCH_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(separatorIndex + 1).trim() : null;
    }

}
