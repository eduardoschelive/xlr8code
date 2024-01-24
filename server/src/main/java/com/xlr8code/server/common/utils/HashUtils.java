package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HashUtils {

    @RequiredArgsConstructor
    @Getter
    public enum Algorithm {
        HMAC_SHA512("HmacSHA512");

        private final String value;
    }

    private static Mac getMAC(Algorithm algorithm, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        var algorithmName = algorithm.getValue();

        var byteKeys = key.getBytes(StandardCharsets.UTF_8);
        var hmac = Mac.getInstance(algorithmName);
        var keySpec = new SecretKeySpec(byteKeys, algorithmName);

        hmac.init(keySpec);

        return hmac;
    }

    public static String hash(String message, String key, Algorithm algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        var mac = getMAC(algorithm, key);
        var messageBytes = message.getBytes(StandardCharsets.UTF_8);
        var macData = mac.doFinal(messageBytes);

        return Base64.getEncoder().encodeToString(macData);
    }

}
