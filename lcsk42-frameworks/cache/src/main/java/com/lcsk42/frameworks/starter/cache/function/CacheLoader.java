package com.lcsk42.frameworks.starter.cache.function;

/**
 * Represents a callback that provides a value to be loaded into the cache when the current value is missing or stale.
 *
 * <p>
 * This functional interface abstracts the logic required to fetch or compute a value, typically used in
 * cache-miss scenarios to populate the cache with fresh data.
 *
 * @param <T> the type of value to be loaded into the cache
 */
@FunctionalInterface
public interface CacheLoader<T> {

    /**
     * Loads and returns the value to be cached.
     *
     * @return the value to be cached, must not be {@code null} unless explicitly allowed by the cache strategy
     */
    T get();
}
