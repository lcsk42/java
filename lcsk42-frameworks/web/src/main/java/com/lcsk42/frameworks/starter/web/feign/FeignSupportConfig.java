package com.lcsk42.frameworks.starter.web.feign;

import feign.Retryer;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

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
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN));

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
}
