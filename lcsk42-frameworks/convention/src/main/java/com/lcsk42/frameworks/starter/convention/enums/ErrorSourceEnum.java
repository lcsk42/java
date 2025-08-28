package com.lcsk42.frameworks.starter.convention.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing the source of an error in the system.
 * Each enum value corresponds to a specific error origin with a standardized code.
 */
@Getter
@AllArgsConstructor
public enum ErrorSourceEnum implements BaseEnum<String> {
    /**
     * Error originated from the client application or input.
     */
    CLIENT("C", "Client"),

    /**
     * Error originated from a remote system or external service.
     */
    REMOTE("R", "Remote"),

    /**
     * Error originated within our own service.
     */
    SERVICE("S", "Service");

    /**
     * The standardized code representing this error source.
     * Used for concise identification in logs and error responses.
     */
    private final String value;

    private final String description;
}
