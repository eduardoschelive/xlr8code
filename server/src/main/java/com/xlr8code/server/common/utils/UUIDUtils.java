package com.xlr8code.server.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {

    /**
     * Converts the given string to a {@link UUID} object.
     *
     * @param string the string to be converted to a {@link UUID} object
     * @return an {@link Optional} containing the {@link UUID} object corresponding to the given string, or an empty {@link Optional} if the given string is not a valid {@link UUID}
     */
    public static Optional<UUID> convertFromString(String string) {
        try {
            return Optional.of(UUID.fromString(string));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
