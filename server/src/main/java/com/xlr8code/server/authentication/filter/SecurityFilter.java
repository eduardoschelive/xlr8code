package com.xlr8code.server.authentication.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlr8code.server.authentication.service.CustomUserDetailsService;
import com.xlr8code.server.authentication.service.TokenService;
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

    private static final String PREFIX = "Bearer";
    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var validatedToken = tokenService.validateAccessToken(token);

        if (validatedToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var username = validatedToken.getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");

        if (token == null || !token.startsWith(PREFIX)) {
            return null;
        }

        return this.removePrefix(token);
    }

    private String removePrefix(String token) {
        return token.substring(PREFIX.length() + 1);
    }

}
