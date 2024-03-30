package com.xlr8code.server.authentication.filter;

import com.xlr8code.server.authentication.service.UserSessionService;
import com.xlr8code.server.authentication.utils.SessionCookieUtils;
import com.xlr8code.server.common.exception.ApplicationException;
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

    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var sessionCookie = SessionCookieUtils.getSessionTokenFromRequest(request);

        if (sessionCookie != null) {
            try {
                var userSession = userSessionService.validateSessionToken(sessionCookie);
                UserDetails userDetails = userSession.getUser();

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ApplicationException e) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}