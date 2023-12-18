package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

