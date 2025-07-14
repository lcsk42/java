package com.lcsk42.frameworks.starter.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.lcsk42.frameworks.starter.web.GlobalExceptionHandler;
import com.lcsk42.frameworks.starter.web.GlobalResultHandler;
import com.lcsk42.frameworks.starter.web.initialize.InitializeDispatcherServletController;
import com.lcsk42.frameworks.starter.web.initialize.InitializeDispatcherServletHandler;
import com.lcsk42.frameworks.starter.web.initialize.PortHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Web Auto Configuration class that sets up common web-related beans and utilities.
 */
public class WebAutoConfiguration {

    /**
     * DispatcherServlet initialization endpoint path.
     */
    public static final String INITIALIZE_PATH = "/initialize/dispatcher-servlet";

    /**
     * Global exception handler to intercept all controller-level exceptions.
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * Global result handler to unify API response format.
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalResultHandler globalResultHandler() {
        return new GlobalResultHandler();
    }

    /**
     * Customized Jackson ObjectMapper bean.
     * - Ignores unknown properties during deserialization.
     * - Excludes null values during serialization.
     * - Serializes Java 8 date/time in ISO format.
     * - Serializes Long as String to avoid JS number precision loss.
     */
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();

        // Add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        // Configure Java 8 date/time module with ISO format serializers/deserializers
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ISO_TIME));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ISO_TIME));

        // Convert Long and long values to strings to avoid JavaScript number precision issues
        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(longModule);

        return objectMapper;
    }

    /**
     * Initializes a lightweight controller used to trigger DispatcherServlet early.
     */
    @Bean
    public InitializeDispatcherServletController initializeDispatcherServletController() {
        return new InitializeDispatcherServletController();
    }

    /**
     * PortHolder bean to hold the web server port.
     * This is used to initialize the DispatcherServlet early.
     */
    @Bean
    public PortHolder portHolder() {
        return new PortHolder();
    }

    /**
     * RestTemplate bean with custom HTTP client factory.
     */
    @Bean
    public RestTemplate simpleRestTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    /**
     * Basic ClientHttpRequestFactory with timeout settings.
     * Improves fault tolerance and responsiveness.
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5 * 1_000);
        factory.setConnectTimeout(5 * 1_000);
        return factory;
    }

    /**
     * CommandLineRunner bean to call the DispatcherServlet initialization endpoint
     * immediately after Spring Boot starts, reducing the first-response latency.
     */
    @Bean
    public InitializeDispatcherServletHandler initializeDispatcherServletHandler(
            RestTemplate simpleRestTemplate,
            PortHolder portHolder,
            ConfigurableEnvironment configurableEnvironment
    ) {
        return new InitializeDispatcherServletHandler(simpleRestTemplate, portHolder, configurableEnvironment);
    }

    /**
     * Basic WebMvcConfigurer implementation for future extensibility (e.g., CORS, formatters, interceptors).
     */
    @Bean
    public WebConfiguration webConfig() {
        return new WebConfiguration();
    }
}
