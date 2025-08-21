package com.lcsk42.frameworks.starter.convention.exception;

import com.lcsk42.frameworks.starter.convention.errorcode.impl.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;

/**
 * RemoteException
 * <p>
 * Represents an exception that occurs during remote or third-party service interactions.
 * It encapsulates a standardized {@link ErrorCode} for consistent error handling.
 */
public class RemoteException extends AbstractException {

    /**
     * Constructs a default remote exception using {@link BaseErrorCode#REMOTE_ERROR}.
     */
    public RemoteException() {
        this(BaseErrorCode.REMOTE_ERROR);
    }

    /**
     * Constructs a remote exception using a specific error code.
     *
     * @param errorCode the associated error code.
     */
    public RemoteException(ErrorCode errorCode) {
        this(null, null, errorCode);
    }

    /**
     * Constructs a remote exception with a custom message and default remote error code.
     *
     * @param message the exception message.
     */
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    /**
     * Constructs a remote exception with a custom message and specific error code.
     *
     * @param message   the exception message.
     * @param errorCode the associated error code.
     */
    public RemoteException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    /**
     * Constructs a remote exception with full parameters.
     *
     * @param message   the exception message.
     * @param throwable the underlying cause of the exception.
     * @param errorCode the associated error code.
     */
    public RemoteException(String message, Throwable throwable, ErrorCode errorCode) {
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
                RemoteException {
                    code='%s',
                    message='%s'
                }""".formatted(errorCode, errorMessage);
    }
}
