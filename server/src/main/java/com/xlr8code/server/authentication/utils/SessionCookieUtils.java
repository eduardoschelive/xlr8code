package com.xlr8code.server.authentication.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public class SessionCookieUtils {

    public static final String SESSION_TOKEN_COOKIE_NAME = "session-token";

    /**
     * @param sessionToken the session token to be used
     * @param maxAge       the maximum age of the cookie in seconds
     *                     (if negative, the cookie is deleted when the browser is closed)
     *                     (if zero, the cookie is deleted immediately)
     *                     (if positive, the cookie is deleted after the specified number of seconds)
     *                     Cannot be null.
     * @return the created session token cookie
     */
    public static String createSessionToken(String sessionToken, long maxAge) {
        return ResponseCookie.from(SESSION_TOKEN_COOKIE_NAME, sessionToken)
                .maxAge(maxAge)
                .httpOnly(true)
                .path("/")
                .build()
                .toString();
    }

    /**
     * @param request the request to get the session token from
     * @return the session token from the request
     * (null if the session token is not found)
     */
    public static String getSessionTokenFromRequest(HttpServletRequest request) {
        var requestCookies = request.getCookies();
        if (requestCookies != null) {
            for (var cookie : requestCookies) {
                if (cookie.getName().equals(SESSION_TOKEN_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
