package com.xlr8code.server.authentication.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoint {

    private static final String PREFIX = "/api";
    public static final String AUTHENTICATION = PREFIX + "/authentication";
    public static final String FAKE = PREFIX + "/fake";

}
