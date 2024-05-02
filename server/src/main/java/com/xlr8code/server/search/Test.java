package com.xlr8code.server.search;

import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.user.entity.User;

import java.util.Map;

public class Test {

    public static void main(String[] args) throws SearchException {
        Map<String, String> queryMap = Map.of("email", "John");

        new QueryParser(queryMap, User.class);
    }

}
