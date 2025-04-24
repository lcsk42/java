package com.lcsk42.frameworks.starter.convention.errorcode;

import lombok.AllArgsConstructor;

/**
 * BaseErrorCode
 * <p>
 * Defines a standard set of error codes and messages categorized by system layers:
 * - A: Client-side errors
 * - B: Service/internal errors
 * - C: External/remote service errors
 */
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {

    /**
     * Generic client-side error.
     */
    CLIENT_ERROR("A-CLIENT-001", "Client-side error"),

    /**
     * Generic system execution error.
     */
    SERVICE_ERROR("B-SERVICE-001", "System execution error"),

    /**
     * Error occurred during Feign client request.
     */
    SERVICE_FEIGN_ERROR("B-SERVICE-002", "Feign request failed"),

    /**
     * Failure when calling a third-party remote service.
     */
    REMOTE_ERROR("C-REMOTE-001", "Failed to call third-party service"),
    ;


    /**
     * Error code identifier.
     */
    private final String code;

    /**
     * Human-readable error message.
     */
    private final String message;

    /**
     * Returns the error code.
     *
     * @return the code string.
     */
    @Override
    public String code() {
        return code;
    }

    /**
     * Returns the error message.
     *
     * @return the message string.
     */
    @Override
    public String message() {
        return message;
    }
}
