package com.xlr8code.server.common.utils;

import lombok.AllArgsConstructor;

import java.security.SecureRandom;

@AllArgsConstructor
public class RandomCode {

    private String allowedCharacters;

    public String generate(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(this.allowedCharacters.charAt(random.nextInt(this.allowedCharacters.length())));
        }
        return sb.toString();
    }

}

