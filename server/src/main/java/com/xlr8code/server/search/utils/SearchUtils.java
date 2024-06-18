package com.xlr8code.server.search.utils;

import com.xlr8code.server.search.annotation.NestedSearchable;
import com.xlr8code.server.search.annotation.Searchable;
import com.xlr8code.server.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtils {

    public static Map<String, Pair<Searchable, Class<?>>> extractSearchableFields(Class<?> targetClass) {
        Map<String, Pair<Searchable, Class<?>>> searchableFields = new HashMap<>();
        Deque<String> fieldPathStack = new ArrayDeque<>();
        traverseFields(fieldPathStack, targetClass, searchableFields);
        return searchableFields;
    }

    private static void traverseFields(Deque<String> fieldPathStack, Class<?> currentClass, Map<String, Pair<Searchable, Class<?>>> searchableFields) {
        for (Field field : currentClass.getDeclaredFields()) {
            evaluateField(field, fieldPathStack, searchableFields);
        }
    }

    private static void evaluateField(Field field, Deque<String> fieldPathStack, Map<String, Pair<Searchable, Class<?>>> searchableFields) {
        if (field.isAnnotationPresent(Searchable.class)) {
            String fullPath = constructFieldPath(fieldPathStack, field.getName());
            searchableFields.put(fullPath, Pair.of(field.getAnnotation(Searchable.class), field.getType()));
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
        var parameterizedType = (ParameterizedType) type;
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

}
