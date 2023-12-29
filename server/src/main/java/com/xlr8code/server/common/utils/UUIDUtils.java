package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {

    public static Optional<UUID> fromString(String string) {
        try {
            return Optional.of(UUID.fromString(string));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
