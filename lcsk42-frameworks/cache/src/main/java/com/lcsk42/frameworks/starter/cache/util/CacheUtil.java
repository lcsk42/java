package com.lcsk42.frameworks.starter.cache.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.Stream;

public final class CacheUtil {

    private static final String SPLICING_OPERATOR = ":";

    /**
     * Build a cache key by joining the provided keys with an underscore.
     * Throws RuntimeException if any key is null or empty.
     *
     * @param keys keys to join into a cache key
     * @return the constructed cache key
     */
    public static String buildKey(String... keys) {
        Stream.of(keys)
                .forEach(key -> {
                    if (StringUtils.isBlank(key)) {
                        throw new RuntimeException("Cache key part must not be null or empty");
                    }
                });
        // Apache Commons Text StringJoiner does not exist, so use StringUtils.join
        return StringUtils.join(keys, SPLICING_OPERATOR);
    }

    /**
     * Checks whether the given cache value is null or a blank string.
     *
     * @param cacheVal the value to check
     * @return true if the value is null or blank, false otherwise
     */
    public static boolean isNullOrBlank(Object cacheVal) {
        return Objects.isNull(cacheVal) || (cacheVal instanceof String && StringUtils.isBlank((String) cacheVal));
    }
}