package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoint {

    public record Authentication() {
        public static final String BASE_PATH = "/authentication";

        public static final String SIGN_IN = "/sign-in";
        public static final String SIGN_UP = "/sign-up";
        public static final String SIGN_OUT = "/sign-out";

        public static final String REFRESH_SESSION = "/refresh";

        public static final String ACTIVATE_USER = "/activate";
        public static final String RESEND_ACTIVATION_CODE = "/resend-activation-code";

        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String RESET_PASSWORD = "/reset-password";
    }

    public record User() {
        public static final String BASE_PATH = "/users";

        public static final String METADATA = "/metadata";
        public static final String PREFERENCES = "/preferences";
        public static final String PASSWORD = "/password";
    }

    public record Categories() {
        public static final String BASE_PATH = "/categories";
    }

    public record Article() {
        public static final String BASE_PATH = "/articles";
    }

    public record Documentation() {
        public static final String BASE_PATH = "/documentation";
    }

}
