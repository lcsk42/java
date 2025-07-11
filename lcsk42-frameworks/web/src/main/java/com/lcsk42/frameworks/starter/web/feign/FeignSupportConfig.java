package com.lcsk42.frameworks.starter.web.feign;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FeignSupportConfig {

    // Factory for creating HttpMessageConverters
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * Configures the Feign decoder to handle the response from Feign clients.
     * It combines multiple decoders, including optional support for responses.
     */
    @Bean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        return new OptionalDecoder(
                new ResponseEntityDecoder(
                        new FeignResultDecoder(
                                new SpringDecoder(this.messageConverters, customizers)
                        )
                )
        );
    }

    /**
     * Configures a RestTemplate bean with support for "text/html" and "text/plain" content types.
     * This is necessary because RestTemplate by default does not support "text/html;charset=UTF-8".
     *
     * @return RestTemplate instance with customized message converters.
     */
    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();

        // Creates a message converter for handling JSON and text-based content types.
        final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();

        // Adds support for both "text/html" and "text/plain" content types.
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                List.of(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN)
        );

        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

    /**
     * Configures the Feign client to never retry failed requests.
     * This helps to prevent automatic retries on failures.
     *
     * @return Feign retryer configured to never retry.
     */
    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    /**
     * Registers the DispatcherServlet with the Spring application context.
     * This is necessary for handling incoming HTTP requests.
     *
     * @param servlet The DispatcherServlet instance to register.
     * @return ServletRegistrationBean for the DispatcherServlet.
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * Configures a RequestInterceptor for Feign clients to forward HTTP headers
     * from the incoming request to the outgoing Feign request.
     * This is useful for passing authentication tokens or other headers.
     *
     * @return RequestInterceptor that copies headers from the current request.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template ->
                Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                        .filter(ServletRequestAttributes.class::isInstance)
                        .map(ServletRequestAttributes.class::cast)
                        .map(ServletRequestAttributes::getRequest)
                        .ifPresent(request -> {
                            Collections.list(request.getHeaderNames()).stream()
                                    .filter(name -> StringUtils.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH, name))
                                    .forEach(name -> template.header(name, request.getHeader(name)));
                        });
    }
}
