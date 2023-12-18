package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomCode {

    private static final String DEFAULT_ALLOWED_CHARACTERS = "0123456789";

    public static String generate(int length) {
        return generate(length, DEFAULT_ALLOWED_CHARACTERS);
    }

    public static String generate(int length, String allowedCharacters) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(allowedCharacters.charAt(random.nextInt(allowedCharacters.length())));
        }
        return sb.toString();
    }

}

