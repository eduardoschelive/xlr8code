package com.xlr8code.server.search;

import com.xlr8code.server.search.exception.SearchException;
import com.xlr8code.server.user.entity.User;

import java.util.Map;

public class Test {

    public static void main(String[] args) throws SearchException {
        Map<String, String> queryMap = Map.of("username_eq", "Doe", "active_eq", "true");

        var time = System.currentTimeMillis();
        var predicate = new FilterSpecification<>(queryMap, User.class);
        System.out.println(predicate);
        System.out.println("Time taken: " + (System.currentTimeMillis() - time) + "ms");
    }
}
