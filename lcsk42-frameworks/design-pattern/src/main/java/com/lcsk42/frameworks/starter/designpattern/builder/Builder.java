package com.lcsk42.frameworks.starter.designpattern.builder;

import java.io.Serializable;

/**
 * Builder Pattern Abstract Interface
 *
 * <p>
 * This interface defines the contract for a builder that is used to create complex objects.
 * The builder pattern allows for the construction of an object step by step,
 * providing a way to construct an object with various configurations and parameters.
 * </p>
 *
 * @param <T> The type of object that the builder will create.
 */
public interface Builder<T> extends Serializable {

    /**
     * Build method
     *
     * <p>
     * This method will assemble the object and return the fully constructed instance.
     * The builder is expected to gather the necessary data during its lifecycle and
     * use that data to build and return the final object of type {@code T}.
     * </p>
     *
     * @return The built object of type {@code T}.
     */
    T build();
}
