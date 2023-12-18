package com.xlr8code.server.authentication.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoint {

    private static final String API_PREFIX = "/api";

    public record Authentication() {
        public static final String BASE_PATH = API_PREFIX + "/authentication";
        public static final String SIGN_IN = "/sign-in";
        public static final String SIGN_UP = "/sign-up";
        public static final String REFRESH_TOKEN = "/refresh";
        public static final String REVOKE_TOKEN = "/revoke";
        public static final String ACTIVATE_USER = "/activate";
        public static final String RESEND_ACTIVATION_CODE = "/resend-activation-code";
    }

}
