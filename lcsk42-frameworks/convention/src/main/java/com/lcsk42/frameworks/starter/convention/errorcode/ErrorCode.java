package com.lcsk42.frameworks.starter.convention.errorcode;

/**
 * Error Code
 * <p>
 * Defines a contract for standardized error code representations.
 */
public interface ErrorCode {
    /**
     * Gets the unique error code.
     *
     * @return the error code as a string.
     */
    String code();

    /**
     * Gets the descriptive error message.
     *
     * @return the error message associated with the code.
     */
    String message();
}
