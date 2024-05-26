package com.xlr8code.server.search;

import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.user.entity.User;

import java.util.Map;

public class Test {

    public static void main(String[] args) throws SearchException {
        Map<String, String> queryMap = Map.of("roles.userRole_eq", "John", "username_eq", "Doe", "roles.userRole_ct", "Jane", "roles.userRole_sort", "ASC", "_page", "1", "_size", "10");

        var time = System.currentTimeMillis();
        new QueryParser(queryMap, User.class).parseQueryParameters();
        System.out.println("Time taken: " + (System.currentTimeMillis() - time) + "ms");
    }

}
