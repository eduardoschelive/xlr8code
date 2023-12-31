package com.xlr8code.server.authentication.filter;

import com.xlr8code.server.authentication.exception.InvalidTokenException;
import com.xlr8code.server.authentication.service.AccessTokenService;
import com.xlr8code.server.authentication.service.CustomUserDetailsService;
import com.xlr8code.server.common.utils.UUIDUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PREFIX = "Bearer";

    private final AccessTokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var token = recoverToken(request);

        var validatedToken = tokenService.validate(token);

        if (validatedToken != null) {
            var uuid = UUIDUtils.fromString(validatedToken.getSubject())
                    .orElseThrow(InvalidTokenException::new);
            UserDetails userDetails = customUserDetailsService.loadUserById(uuid);

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Nullable
    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (!this.isTokenCandidate(token)) {
            return null;
        }

        return this.removePrefix(token);
    }

    private boolean isTokenCandidate(String token) {
        return token != null && token.startsWith(PREFIX);
    }

    private String removePrefix(String token) {
        return token.substring(PREFIX.length() + 1);
    }

}
