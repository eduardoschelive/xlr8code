package com.xlr8code.server.authentication.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoint {

    private static final String API_PREFIX = "/api";

    public record Authentication() {
        public static final String BASE_PATH = API_PREFIX + "/authentication";
        public static final String SIGN_IN = BASE_PATH + "/sign-in";
        public static final String SIGN_UP = BASE_PATH + "/sign-up";
    }

}
