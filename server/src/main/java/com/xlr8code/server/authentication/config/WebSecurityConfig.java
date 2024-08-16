package com.xlr8code.server.authentication.config;

import com.xlr8code.server.authentication.filter.SecurityFilter;
import com.xlr8code.server.authentication.utils.EndpointSecurityDetails;
import com.xlr8code.server.authentication.utils.EndpointSecurityUtils;
import com.xlr8code.server.user.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Value("${application.documentation-endpoint}")
    private String documentationEndpoint;

    private void configureEndpointSecurity(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        EndpointSecurityUtils.ENDPOINT_SECURITY.forEach((path, securityDetails) ->
                securityDetails.forEach(detail ->
                        configureSecurity(authorizeRequests, detail, path)
                )
        );

        authorizeRequests.requestMatchers(documentationEndpoint + "/**").permitAll();
        authorizeRequests.requestMatchers("/error/**").permitAll();

        authorizeRequests.anyRequest().denyAll();
    }

    private void configureSecurity(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests, EndpointSecurityDetails detail, String path) {
        var configurer = authorizeRequests.requestMatchers(detail.method(), path);
        var minimumRole = detail.minimumRole();

        if (minimumRole != null) {
            configurer.hasRole(minimumRole.name());
            return;
        }
        configurer.permitAll();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        configure(httpSecurity);
        configureEndpoints(httpSecurity);
        configureFilters(httpSecurity);
        configureEntryPoint(httpSecurity);

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
                .authorizeHttpRequests(this::configureEndpointSecurity);
    }

    private void configureFilters(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(
                this.securityFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }

    private void configureEntryPoint(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.exceptionHandling(
                exceptionHandling -> exceptionHandling.authenticationEntryPoint(this.restAuthenticationEntryPoint)
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
