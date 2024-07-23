package com.xlr8code.server.common.config;

import com.xlr8code.server.common.helper.ApplicationLocaleResolver;
import com.xlr8code.server.common.interceptor.ContentLanguageInterceptor;
import com.xlr8code.server.common.service.LocaleService;
import com.xlr8code.server.filter.resolver.FilterEndpointPageableResolver;
import com.xlr8code.server.filter.resolver.FilterEndpointSpecificationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LocaleService localeService;

    @Bean
    public LocaleResolver localeResolver() {
        return new ApplicationLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    @Bean
    public ContentLanguageInterceptor contentLanguageInterceptor() {
        return new ContentLanguageInterceptor(localeService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(localeChangeInterceptor());
        interceptorRegistry.addInterceptor(contentLanguageInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/documentation").setViewName("documentation/index");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FilterEndpointPageableResolver());
        resolvers.add(new FilterEndpointSpecificationResolver());
    }
}
