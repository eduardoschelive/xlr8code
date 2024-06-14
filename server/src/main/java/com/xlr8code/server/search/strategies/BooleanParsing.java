package com.xlr8code.server.search.strategies;

public class BooleanParsing implements ParsingStrategy<Boolean> {
    @Override
    public Boolean parse(Object value) {
        return Boolean.parseBoolean(value.toString());
    }

}
