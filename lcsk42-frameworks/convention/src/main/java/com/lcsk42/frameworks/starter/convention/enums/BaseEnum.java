package com.lcsk42.frameworks.starter.convention.enums;

import java.util.Objects;

/**
 * A generic interface for enums that have a value and description.
 *
 * @param <T> the type of the enum's value
 */
public interface BaseEnum<T> {

    /**
     * Gets the enum's underlying value.
     *
     * @return the value associated with this enum constant
     */
    T getValue();

    /**
     * Gets the human-readable description of the enum.
     *
     * @return the descriptive text for this enum constant
     */
    String getDescription();

    /**
     * Finds an enum constant by its value.
     *
     * @param <E>       the enum type that implements BaseEnum
     * @param <T>       the type of the value
     * @param value     the value to search for
     * @param enumClass the enum class to search in
     * @return the matching enum constant, or null if not found
     */
    static <E extends Enum<E> & BaseEnum<T>, T> E fromValue(T value, Class<E> enumClass) {
        Objects.requireNonNull(enumClass, "Enum class cannot be null");

        for (E enumConstant : enumClass.getEnumConstants()) {
            if (Objects.equals(enumConstant.getValue(), value)) {
                return enumConstant;
            }
        }
        return null;
    }

    /**
     * Finds an enum constant by its description.
     *
     * @param <E>         the enum type that implements BaseEnum
     * @param description the description to search for
     * @param enumClass   the enum class to search in
     * @return the matching enum constant, or null if not found
     */
    static <E extends Enum<E> & BaseEnum<?>> E fromDescription(String description, Class<E> enumClass) {
        Objects.requireNonNull(enumClass, "Enum class cannot be null");

        for (E enumConstant : enumClass.getEnumConstants()) {
            if (Objects.equals(enumConstant.getDescription(), description)) {
                return enumConstant;
            }
        }
        return null;
    }

    /**
     * Checks if a given value is valid for the specified enum class.
     *
     * @param <E>       the enum type that implements BaseEnum
     * @param <T>       the type of the value
     * @param value     the value to check
     * @param enumClass the enum class to validate against
     * @return true if the value exists in the enum, false otherwise
     */
    default <E extends Enum<E> & BaseEnum<T>> boolean isValidValue(T value, Class<E> enumClass) {
        return fromValue(value, enumClass) != null;
    }
}

