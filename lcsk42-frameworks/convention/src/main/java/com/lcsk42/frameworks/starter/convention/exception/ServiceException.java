package com.lcsk42.frameworks.starter.convention.exception;

import com.lcsk42.frameworks.starter.convention.errorcode.impl.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;

import java.util.Optional;

/**
 * ServiceException
 * <p>
 * Represents an exception that occurs during internal service logic execution.
 * It wraps an {@link ErrorCode} to provide structured error information.
 */
public class ServiceException extends AbstractException {

    /**
     * Constructs a default service exception using {@link BaseErrorCode#SERVICE_ERROR}.
     */
    public ServiceException() {
        this(BaseErrorCode.SERVICE_ERROR);
    }

    /**
     * Constructs a service exception using a specific error code.
     *
     * @param errorCode the associated error code.
     */
    public ServiceException(ErrorCode errorCode) {
        this(null, errorCode);
    }

    /**
     * Constructs a service exception with a custom message and default service error code.
     *
     * @param message the exception message.
     */
    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    /**
     * Constructs a service exception with a custom message and a specific error code.
     *
     * @param message   the exception message.
     * @param errorCode the associated error code.
     */
    public ServiceException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    /**
     * Constructs a service exception with full parameters.
     * If message is null, the message from the error code will be used.
     *
     * @param message   the exception message.
     * @param throwable the underlying cause of the exception.
     * @param errorCode the associated error code.
     */
    public ServiceException(String message, Throwable throwable, ErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.getMessage()), throwable, errorCode);
    }

    /**
     * Returns a string representation of the exception.
     *
     * @return a string including the error code and message.
     */
    @Override
    public String toString() {
        return """
                ServiceException {
                    code='%s',
                    message='%s'
                }""".formatted(errorCode, errorMessage);
    }
}
