package com.lcsk42.frameworks.starter.convention.exception;

import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * AbstractException
 * <p>
 * A base class for custom runtime exceptions that carry a standardized error code and message.
 */
@Getter
public abstract class AbstractException extends RuntimeException {

    /**
     * Standardized error code representing the type of error.
     */
    public final String errorCode;

    /**
     * Detailed error message describing the exception.
     */
    public final String errorMessage;

    /**
     * Constructs a new AbstractException with a custom message, cause, and error code.
     *
     * @param message   the custom message for the exception.
     * @param throwable the root cause of the exception.
     * @param errorCode the associated error code.
     */
    protected AbstractException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = StringUtils.hasLength(message) ? message : errorCode.message();
    }
}
