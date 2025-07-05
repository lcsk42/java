package com.lcsk42.frameworks.starter.base;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Singleton object container
 * <p>
 * This utility class provides a thread-safe container for managing singleton objects
 * identified by a string key. It is useful for caching and retrieving lazily-initialized
 * objects outside the scope of Spring or other dependency injection frameworks.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Prevents instantiation
public final class Singleton {

    // Thread-safe map for storing singleton instances by string key
    private static final ConcurrentHashMap<String, Object> SINGLE_OBJECT_POOL = new ConcurrentHashMap<>();

    /**
     * Retrieve the singleton object from the container by its key.
     *
     * @param key The key associated with the object.
     * @param <T> The expected type of the object.
     * @return The singleton object, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Object result = SINGLE_OBJECT_POOL.get(key);
        return result == null ? null : (T) result;
    }

    /**
     * Retrieve the singleton object from the container by its key.
     * <p>
     * If the object is not found, it will be created using the provided {@link Supplier}
     * and stored in the container.
     *
     * @param key      The key associated with the object.
     * @param supplier A supplier to create the object if not present.
     * @param <T>      The expected type of the object.
     * @return The existing or newly created singleton object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Supplier<T> supplier) {
        Object result = SINGLE_OBJECT_POOL.get(key);
        if (result == null && (result = supplier.get()) != null) {
            SINGLE_OBJECT_POOL.put(key, result);
        }
        return result != null ? (T) result : null;
    }

    /**
     * Put an object into the container using its class name as the key.
     *
     * @param value The object to be stored.
     */
    public static void put(Object value) {
        put(value.getClass().getName(), value);
    }

    /**
     * Put an object into the container with a specified key.
     *
     * @param key   The key to associate with the object.
     * @param value The object to be stored.
     */
    public static void put(String key, Object value) {
        SINGLE_OBJECT_POOL.put(key, value);
    }
}
