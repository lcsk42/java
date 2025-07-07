package com.lcsk42.frameworks.starter.web;

import com.lcsk42.frameworks.starter.common.util.JacksonUtil;
import com.lcsk42.frameworks.starter.convention.result.Result;
import com.lcsk42.frameworks.starter.web.annotaion.CompatibleOutput;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResultHandler implements ResponseBodyAdvice<Object> {

    /**
     * Determines whether the response body should be processed.
     * This method checks:
     * 1. If the class or method has the @CompatibleOutput annotation.
     * 2. If the return type is of type Result.
     * 3. If the request URI starts with "/api".
     */
    @Override
    public boolean supports(final MethodParameter returnType,
                            @NonNull final Class<? extends HttpMessageConverter<?>> converterType) {
        // Check whether the class has the @CompatibleOutput annotation.
        Class<?> controllerClass = returnType.getContainingClass();
        boolean hasClassAnnotation = AnnotationUtils.findAnnotation(controllerClass, CompatibleOutput.class) != null;
        if (hasClassAnnotation) {
            return false;
        }

        // Check whether the method has the @CompatibleOutput annotation.
        boolean hasMethodAnnotation = returnType.getMethodAnnotation(CompatibleOutput.class) != null;
        if (hasMethodAnnotation) {
            return false;
        }

        // Check whether the return type is Result.class.
        boolean isResultType = returnType.getParameterType().equals(Result.class);
        if (isResultType) {
            return false;
        }

        // Check if the request URI starts with "/api".
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return false;
        }

        HttpServletRequest request = attributes.getRequest();
        String requestUri = request.getRequestURI();

        return StringUtils.startsWith(requestUri, "/api");
    }

    /**
     * Modifies the response body before it is written to the response.
     * It wraps the body in a Result object unless it is a string, in which case it is converted to a JSON result.
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        // If the return type is a string, convert it into a JSON result.
        if (returnType.getParameterType().isAssignableFrom(String.class)) {
            String json = JacksonUtil.toJSON(Result.success(body));
            // Set the content type to application/json.
            // Because returnType.getParameterType() is String, the default content type will be text/plain.
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return ObjectUtils.defaultIfNull(json, body.toString());
        }

        // Otherwise, wrap the body in a Result object.
        return Result.success(body);
    }
}
