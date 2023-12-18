package com.xlr8code.server.common.helper;

import com.xlr8code.server.common.utils.RandomCode;
import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

    private static final String ALLOWED_CHARACTERS = "0123456789";
    private final RandomCode randomCode = new RandomCode(ALLOWED_CHARACTERS);

    public String generateActivationCode() {
        return this.randomCode.generate(6);
    }

    public String generatePasswordResetCode() {
        return this.randomCode.generate(6);
    }

}
