package com.xlr8code.server.openapi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class OpenAPIWebMvcConfig implements WebMvcConfigurer {

    @Value("${application.documentation-endpoint}")
    private String documentationEndpoint;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(documentationEndpoint).setViewName("documentation/index");
    }

}
