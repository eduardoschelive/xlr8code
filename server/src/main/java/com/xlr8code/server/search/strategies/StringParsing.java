package com.xlr8code.server.search.strategies;

public class StringParsing implements ParsingStrategy<String> {
    @Override
    public String parse(Object value) {
        return value.toString();
    }

}
