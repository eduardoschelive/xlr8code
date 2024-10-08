package com.xlr8code.server.filter.utils;

import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.filter.annotation.Filterable;
import com.xlr8code.server.filter.annotation.NestedFilterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static com.xlr8code.server.filter.utils.FilterConstants.FILTER_PARAM_SEPARATOR;

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
        for (var field : getAllFieldsUpTo(currentClass, Object.class)) {
            evaluateField(field, fieldPathStack, fieldDetailsHashMap);
        }
    }

    private static Iterable<Field> getAllFieldsUpTo(@NonNull Class<?> startClass, @NonNull Class<?> exclusiveParent) {
        var currentClassFields = new ArrayList<>(Arrays.asList(startClass.getDeclaredFields()));
        var parentClass = startClass.getSuperclass();

        if (parentClass != null && !parentClass.equals(exclusiveParent)) {
            currentClassFields.addAll((List<Field>) getAllFieldsUpTo(parentClass, exclusiveParent));
        }

        return currentClassFields;
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
        int separatorIndex = key.lastIndexOf(FILTER_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(0, separatorIndex).trim() : null;
    }

    /**
     * @param key the key to extract the operation from
     * @return the operation extracted from the key
     */
    public static String extractOperation(String key) {
        int separatorIndex = key.lastIndexOf(FILTER_PARAM_SEPARATOR);
        return (separatorIndex != -1) ? key.substring(separatorIndex + 1).trim() : null;
    }

    /**
     * @param method the method to extract the filterable fields from
     * @return a map of filterable fields and their details
     * @throws IllegalArgumentException if the method is not annotated with @FilterEndpoint
     */
    public static Map<String, FilterFieldDetails> extractFilterableFieldsFromMethod(MethodParameter method) {
        var annotation = method.getMethodAnnotation(FilterEndpoint.class);

        if (annotation == null) {
            throw new IllegalArgumentException("Method must be annotated with @FilterEndpoint");
        }

        return extractFilterableFields(annotation.value());
    }

    /**
     * @param webRequest the web request to extract the query parameters from
     * @return the query parameters extracted from the web request
     * @see QueryParameterDetails
     */
    public static QueryParameterDetails extractQueryParameters(WebRequest webRequest) {
        var queryParamsMap = new HashMap<String, String>();
        webRequest.getParameterNames().forEachRemaining(name -> queryParamsMap.put(name, webRequest.getParameter(name)));
        return new QueryParameterDetails(queryParamsMap);
    }

    /**
     * @param page the page to build the response entity from
     * @param <T>  the type of the page
     * @return the response entity built from the page
     */
    public static <T> ResponseEntity<PagedModel<T>> buildResponseEntity(PagedModel<T> page) {
        var responseHeaders = new HttpHeaders();
        var pageMetadata = page.getMetadata();

        if (pageMetadata != null) {
            var totalElements = pageMetadata.totalElements();
            responseHeaders.add(FilterConstants.X_FILTER_RESULT_SIZE_HEADER, String.valueOf(totalElements));
        }

        return ResponseEntity.ok().headers(responseHeaders).body(page);
    }

}
