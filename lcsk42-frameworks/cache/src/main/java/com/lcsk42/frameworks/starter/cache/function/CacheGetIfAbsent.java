package com.lcsk42.frameworks.starter.cache.function;

/**
 * Callback to be executed when a cache lookup returns null or is considered invalid.
 *
 * <p>
 * This functional interface is typically used to define custom actions to perform when the cached value
 * is missing, expired, or filtered out. It allows deferred operations such as logging, statistics, or
 * triggering a data load.
 *
 * @param <T> the type of the parameter passed to the callback
 */
@FunctionalInterface
public interface CacheGetIfAbsent<T> {

    /**
     * Executes custom logic when the cache is missing or the value is invalid.
     *
     * @param param an optional parameter, can be used to assist the fallback logic
     */
    void accept(T param);
}
