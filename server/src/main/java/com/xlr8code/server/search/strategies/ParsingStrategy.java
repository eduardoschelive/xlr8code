package com.xlr8code.server.search.strategies;

public interface ParsingStrategy<T> {

    T parse(Object value);

}
