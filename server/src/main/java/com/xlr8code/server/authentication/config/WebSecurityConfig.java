package com.xlr8code.server.authentication.config;

import com.xlr8code.server.authentication.filter.SecurityFilter;
import com.xlr8code.server.authentication.utils.Endpoint;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        configure(httpSecurity);
        configureEndpoints(httpSecurity);
        configureFilters(httpSecurity);

        return httpSecurity.build();
    }

    private void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    }

    private void configureEndpoints(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(WebSecurityConfig::configureEndpointSecurity);
    }

    private static void configureEndpointSecurity(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        // TODO: 2021-10-10 change dto of 403 response
        // USER
        authorizeRequests.requestMatchers(HttpMethod.DELETE, Endpoint.User.BASE_PATH + "/**").hasRole(UserRole.MEMBER.name());
        authorizeRequests.requestMatchers(HttpMethod.PUT, Endpoint.User.BASE_PATH + "/**").hasRole(UserRole.MEMBER.name());
        authorizeRequests.anyRequest().permitAll();
    }

    private void configureFilters(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(
                this.securityFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        var roleHierarchy = new RoleHierarchyImpl();

        var hierarchy = UserRole.getHierarchy();
        roleHierarchy.setHierarchy(hierarchy);

        return roleHierarchy;
    }

}
