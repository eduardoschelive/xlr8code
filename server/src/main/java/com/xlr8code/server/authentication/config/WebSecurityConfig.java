package com.xlr8code.server.authentication.config;

import com.xlr8code.server.authentication.filter.SecurityFilter;
import com.xlr8code.server.authentication.utils.RoleEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        var endpoints = RoleEndpoints.getRoleEndpoints();

        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> {
                    for (var entry : endpoints.entrySet()) {
                        var roleName = entry.getKey().name();
                        var roleEndpoints = entry.getValue();
                        authorizeRequests
                                .requestMatchers(roleEndpoints)
                                .hasRole(roleName);
                    }
                    authorizeRequests.anyRequest().permitAll();
                });
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

}
