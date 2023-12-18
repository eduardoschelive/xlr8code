package com.xlr8code.server.authentication.filter;

import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.authentication.service.CustomUserDetailsService;
import com.xlr8code.server.authentication.service.TokenService;
import com.xlr8code.server.common.exception.ApplicationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PREFIX = "Bearer";
    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        var token = recoverToken(request);
        var validatedToken = tokenService.validateAccessToken(token);

        var username = validatedToken.getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (token == null || !token.startsWith(PREFIX)) {
            throw new ApplicationException(AuthenticationExceptionType.CANT_RETRIEVE_AUTHORIZATION_HEADER);
        }

        return this.removePrefix(token);
    }

    private String removePrefix(String token) {
        return token.substring(PREFIX.length() + 1);
    }

}
