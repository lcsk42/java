package com.lcsk42.frameworks.starter.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = RedisDistributedProperties.PREFIX)
public class RedisDistributedProperties {

    public static final String PREFIX = "framework.cache.redis";

    /**
     * Key prefix
     */
    private String prefix = "";

    /**
     * Charset used for the key prefix
     */
    private String prefixCharset = StandardCharsets.UTF_8.name();

    /**
     * Default timeout for values
     */
    private Long valueTimeout = 30 * 1_000L;

    /**
     * Time unit for the value timeout
     */
    private TimeUnit valueTimeUnit = TimeUnit.MILLISECONDS;
}
