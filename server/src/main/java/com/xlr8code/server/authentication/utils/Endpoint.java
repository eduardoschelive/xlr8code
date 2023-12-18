package com.xlr8code.server.authentication.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoint {

    public static final String SIGN_IN = "/sign-in";
    public static final String SIGN_UP = "/sign-up";
    public static final String VERIFY_TOKEN = "/verify-token";
    private static final String PREFIX = "/api";
    public static final String AUTHENTICATION = PREFIX + "/authentication";
    public static final String FAKE = PREFIX + "/fake";

}
