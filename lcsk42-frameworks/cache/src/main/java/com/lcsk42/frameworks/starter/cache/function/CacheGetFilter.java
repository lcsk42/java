package com.lcsk42.frameworks.starter.cache.function;

/**
 * Cache filtering strategy during retrieval.
 *
 * <p>
 * This functional interface is used to determine whether a cache value should be accepted
 * or rejected during a cache read operation. It's particularly useful in scenarios where
 * cached data may become stale or invalid, and needs to be validated before use.
 *
 * @param <T> the type of the object being filtered
 */
@FunctionalInterface
public interface CacheGetFilter<T> {

    /**
     * Determines whether the given value should be filtered (i.e., considered invalid).
     *
     * @param param the object to validate
     * @return {@code true} if the value should be filtered (rejected), {@code false} if it is valid and can be used
     */
    boolean filter(T param);
}
