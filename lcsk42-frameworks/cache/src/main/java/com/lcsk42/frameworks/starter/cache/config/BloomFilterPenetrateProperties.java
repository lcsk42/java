package com.lcsk42.frameworks.starter.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = BloomFilterPenetrateProperties.PREFIX)
public class BloomFilterPenetrateProperties {

    public static final String PREFIX = "framework.cache.redis.bloom-filter.default";

    /**
     * Default name of the Bloom filter instance
     */
    private String name = "cache_penetration_bloom_filter";

    /**
     * Expected number of insertions per element
     */
    private Long expectedInsertions = 64L;

    /**
     * Expected false positive probability
     */
    private Double falseProbability = 0.03D;
}