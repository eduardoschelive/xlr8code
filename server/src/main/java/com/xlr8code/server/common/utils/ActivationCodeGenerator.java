package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivationCodeGenerator {

    private static final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 8;

    public static String generate() {
        var secureRandom = new SecureRandom();
        var generatedCodeBuilder = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            var randomIndex = secureRandom.nextInt(ALLOWED_CHARS.length());
            var randomChar = ALLOWED_CHARS.charAt(randomIndex);
            generatedCodeBuilder.append(randomChar);
        }

        return generatedCodeBuilder.toString();
    }

}

