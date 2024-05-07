package com.xlr8code.server.search.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ParamHolder<T, V> {

    private final Map<T, List<V>> searchParams = new HashMap<>();

    public void computeIfAbsent(T operation, V value) {
        searchParams.computeIfAbsent(operation, k -> new ArrayList<>()).add(value);
    }

}
