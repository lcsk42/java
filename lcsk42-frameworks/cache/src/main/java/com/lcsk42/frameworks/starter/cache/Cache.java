package com.lcsk42.frameworks.starter.cache;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

/**
 * Unified cache interface definition.
 * <p>
 * This interface provides a consistent API to interact with underlying caching systems
 * (e.g., Redis, Caffeine, etc.). It supports basic operations such as get, put, delete,
 * and existence check.
 */
public interface Cache {

    /**
     * Retrieve an object from the cache by key.
     *
     * @param key   the cache key (must not be blank)
     * @param clazz the expected object type
     * @param <T>   the type of the value
     * @return the cached object or {@code null} if not found
     */
    <T> T get(@NotBlank String key, Class<T> clazz);

    /**
     * Store an object in the cache.
     *
     * @param key   the cache key (must not be blank)
     * @param value the object to store
     */
    void put(@NotBlank String key, Object value);

    /**
     * Store a key-value pair only if all specified keys are currently absent.
     * Used to ensure atomic uniqueness across multiple keys.
     *
     * @param keys a collection of keys to check (must not be null)
     * @return {@code true} if all keys were absent and the put operation succeeded,
     *         {@code false} if any key already exists
     */
    Boolean putIfAllAbsent(@NotNull Collection<String> keys);

    /**
     * Remove an object from the cache by key.
     *
     * @param key the cache key to delete (must not be blank)
     * @return {@code true} if the key was deleted, {@code false} otherwise
     */
    Boolean delete(@NotBlank String key);

    /**
     * Remove multiple keys from the cache.
     *
     * @param keys a collection of keys to delete (must not be null)
     * @return the number of keys successfully deleted
     */
    Long delete(@NotNull Collection<String> keys);

    /**
     * Check if a key exists in the cache.
     *
     * @param key the key to check (must not be blank)
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    Boolean hasKey(@NotBlank String key);

    /**
     * Get the native cache implementation instance (e.g., RedisTemplate, CaffeineCache).
     *
     * @return the underlying cache component
     */
    Object getInstance();
}
