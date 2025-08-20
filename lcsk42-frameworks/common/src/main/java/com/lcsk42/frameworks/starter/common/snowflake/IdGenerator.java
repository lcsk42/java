package com.lcsk42.frameworks.starter.common.snowflake;

import org.apache.commons.lang3.StringUtils;

/**
 * Interface for generating unique identifiers.
 * Provides default implementations for both numeric and string ID generation.
 */
public interface IdGenerator {

    /**
     * Generates and returns the next unique numeric ID.
     * Default implementation returns 0 (should be overridden by implementations).
     *
     * @return the next numeric ID (0 by default)
     */
    default long nextId() {
        return 0L;
    }

    /**
     * Generates and returns the next unique ID as a string.
     * Default implementation returns an empty string (should be overridden by implementations).
     *
     * @return the next ID as string (empty string by default)
     */
    default String nextIdString() {
        return StringUtils.EMPTY;
    }
}