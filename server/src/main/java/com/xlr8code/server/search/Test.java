package com.xlr8code.server.search;

import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.user.entity.User;

import java.util.Map;

public class Test {

    public static void main(String[] args) throws SearchException {
        Map<String, String> queryMap = Map.of("roles.userRole_eq", "John", "usernam_eq", "Doe", "roles.userRole_ct", "Jane", "roles.userRole_sort", "ASC", "_page", "1", "_size", "10");

        new QueryParser(queryMap, User.class).parseQueryParameters();
    }

}
