package com.lcsk42.frameworks.starter.cache;

import com.lcsk42.frameworks.starter.cache.function.CacheGetFilter;
import com.lcsk42.frameworks.starter.cache.function.CacheGetIfAbsent;
import com.lcsk42.frameworks.starter.cache.function.CacheLoader;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.redisson.api.RBloomFilter;

import java.util.concurrent.TimeUnit;

public interface DistributedCache extends Cache {

    /**
     * Get a cache value. If not found, use the provided {@link CacheLoader} to load it.
     */
    <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout);

    /**
     * Get a cache value. If not found, use the provided {@link CacheLoader} to load it.
     */
    <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit);

    /**
     * Safely get a cache value. If not found, use the provided {@link CacheLoader} to load it.
     * Helps prevent cache breakdown and avalanche issues.
     * Suitable for internal interfaces not exposed externally.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout);

    /**
     * Safely get a cache value. If not found, use the provided {@link CacheLoader} to load it.
     * Helps prevent cache breakdown and avalanche issues.
     * Suitable for internal interfaces not exposed externally.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit);

    /**
     * Safely get a cache value using a Bloom filter. If not found, use {@link CacheLoader} to load it.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Suitable for interfaces exposed externally; requires client-provided Bloom filter.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, RBloomFilter<String> bloomFilter);

    /**
     * Safely get a cache value using a Bloom filter. If not found, use {@link CacheLoader} to load it.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Suitable for interfaces exposed externally; requires client-provided Bloom filter.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter);

    /**
     * Safely get a cache value using a Bloom filter and a filter check.
     * If not found, use {@link CacheLoader} to load it.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Also handles the issue that Bloom filters cannot delete entries.
     * Suitable for externally exposed interfaces.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout,
                  RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter);

    /**
     * Safely get a cache value using a Bloom filter and a filter check.
     * If not found, use {@link CacheLoader} to load it.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Also handles the issue that Bloom filters cannot delete entries.
     * Suitable for externally exposed interfaces.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit,
                  RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter);

    /**
     * Safely get a cache value using a Bloom filter, a filter check, and a fallback handler.
     * If not found, use {@link CacheLoader} to load it.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Suitable for externally exposed interfaces.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout,
                  RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter, CacheGetIfAbsent<String> cacheGetIfAbsent);

    /**
     * Safely get a cache value using a Bloom filter, a filter check, and a fallback handler.
     * If not found, use {@link CacheLoader} to load it.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Suitable for externally exposed interfaces.
     */
    <T> T safeGet(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, long timeout, TimeUnit timeUnit,
                  RBloomFilter<String> bloomFilter, CacheGetFilter<String> cacheCheckFilter, CacheGetIfAbsent<String> cacheGetIfAbsent);

    /**
     * Put a value into the cache with a custom expiration time.
     */
    void put(@NotBlank String key, Object value, long timeout);

    /**
     * Put a value into the cache with a custom expiration time.
     */
    void put(@NotBlank String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * Safely put a value into the cache and add the key to a Bloom filter.
     * Helps prevent cache penetration, breakdown, and avalanche.
     * Suitable for externally exposed interfaces.
     */
    void safePut(@NotBlank String key, Object value, long timeout, RBloomFilter<String> bloomFilter);

    /**
     * Safely put a value into the cache with a custom expiration time and add the key to a Bloom filter.
     * Greatly reduces the chance of cache penetration, breakdown, and avalanche.
     * Suitable for externally exposed interfaces.
     */
    void safePut(@NotBlank String key, Object value, long timeout, TimeUnit timeUnit, RBloomFilter<String> bloomFilter);

    /**
     * Count how many of the specified keys exist in the cache.
     */
    Long countExistingKeys(@NotNull String... keys);
}
