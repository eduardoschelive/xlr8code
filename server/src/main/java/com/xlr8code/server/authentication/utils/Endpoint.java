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
        public static final String SIGN_OUT = "/sign-out";

        public static final String REFRESH_SESSION = "/refresh";

        public static final String ACTIVATE_USER = "/activate";
        public static final String RESEND_ACTIVATION_CODE = "/resend-activation-code";

        public static final String FORGOT_PASSWORD = "/forgot-currentPassword";
        public static final String RESET_PASSWORD = "/reset-currentPassword";
    }

    public record User() {
        public static final String BASE_PATH = API_PREFIX + "/user";
    }

}
