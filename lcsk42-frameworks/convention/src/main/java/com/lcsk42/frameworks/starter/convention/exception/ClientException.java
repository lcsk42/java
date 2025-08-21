package com.lcsk42.frameworks.starter.convention.exception;

import com.lcsk42.frameworks.starter.convention.errorcode.impl.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;

/**
 * ClientException
 * <p>
 * Represents an exception that originates from client-side errors.
 * This exception wraps an {@link ErrorCode} to provide standardized error handling.
 */
public class ClientException extends AbstractException {

    /**
     * Constructs a default client exception using {@link BaseErrorCode#CLIENT_ERROR}.
     */
    public ClientException() {
        this(BaseErrorCode.CLIENT_ERROR);
    }

    /**
     * Constructs a client exception using a specific error code.
     *
     * @param errorCode the associated error code.
     */
    public ClientException(ErrorCode errorCode) {
        this(null, null, errorCode);
    }

    /**
     * Constructs a client exception with a custom message and default error code.
     *
     * @param message the exception message.
     */
    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    /**
     * Constructs a client exception with a custom message and specific error code.
     *
     * @param message   the exception message.
     * @param errorCode the associated error code.
     */
    public ClientException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    /**
     * Constructs a client exception with full parameters.
     *
     * @param message   the exception message.
     * @param throwable the underlying cause of the exception.
     * @param errorCode the associated error code.
     */
    public ClientException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    /**
     * Returns a string representation of the exception.
     *
     * @return a string including the error code and message.
     */
    @Override
    public String toString() {
        return """
                ClientException {
                    code='%s',
                    message='%s'
                }""".formatted(errorCode, errorMessage);
    }
}
