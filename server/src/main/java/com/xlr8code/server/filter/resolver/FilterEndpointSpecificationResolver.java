package com.xlr8code.server.filter.resolver;

import com.xlr8code.server.filter.FilterSpecification;
import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.filter.utils.FilterUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FilterEndpointSpecificationResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Specification.class) && parameter.hasMethodAnnotation(FilterEndpoint.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var filterDetails = FilterUtils.extractFilterableFieldsFromMethod(parameter);
        var queryParameters = FilterUtils.extractQueryParameters(webRequest);

        return new FilterSpecification<>(queryParameters.getFilterParameters(), filterDetails);
    }
}
