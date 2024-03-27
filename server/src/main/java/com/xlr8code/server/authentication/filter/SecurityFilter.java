package com.xlr8code.server.authentication.filter;

import com.xlr8code.server.authentication.exception.InvalidTokenException;
import com.xlr8code.server.authentication.service.CustomUserDetailsService;
import com.xlr8code.server.authentication.service.UserSessionService;
import com.xlr8code.server.authentication.utils.SessionCookieUtils;
import com.xlr8code.server.common.utils.UUIDUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var sessionCookie = SessionCookieUtils.getSessionTokenFromRequest(request);

         if (sessionCookie != null) {
             var userSession = userSessionService.validateSessionToken(sessionCookie);
            UserDetails userDetails = userSession.getUser();

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}