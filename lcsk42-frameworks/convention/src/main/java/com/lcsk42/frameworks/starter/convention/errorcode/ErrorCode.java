package com.lcsk42.frameworks.starter.convention.errorcode;

import com.lcsk42.frameworks.starter.convention.enums.BusinessDomainEnum;
import com.lcsk42.frameworks.starter.convention.enums.ErrorSourceEnum;
import com.lcsk42.frameworks.starter.convention.exception.AbstractException;
import com.lcsk42.frameworks.starter.convention.exception.ClientException;
import com.lcsk42.frameworks.starter.convention.exception.RemoteException;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;

/**
 * Defines a standardized contract for error code representations across the system.
 * <p>
 * This interface serves as the foundation for creating consistent error handling by providing:
 * <ul>
 *   <li>Conversion to appropriate exception types ({@link ClientException}, {@link ServiceException}, etc.)</li>
 *   <li>Standardized error code formatting</li>
 *   <li>Clear separation between technical error sources and business domains</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * public enum OrderErrorCodes implements ErrorCode {
 *     INVALID_ORDER(ErrorSourceEnum.CLIENT, BusinessDomainEnum.ORDER, ErrorNumber.CODE_101);
 *
 *     // ... implementation ...
 * }
 * }</pre>
 */
public interface ErrorCode {
    /**
     * Converts this error code to the appropriate exception type based on {@link ErrorSourceEnum}.
     *
     * @return concrete exception instance matching the error source
     * @see #toClientException()
     * @see #toServiceException()
     * @see #toRemoteException()
     */
    default AbstractException toException() {
        ErrorSourceEnum errorSourceEnum = getErrorSourceEnum();
        return switch (errorSourceEnum) {
            case CLIENT -> toClientException();
            case REMOTE -> toRemoteException();
            case SERVICE -> toServiceException();
        };
    }

    /**
     * Creates a {@link ClientException} for client-side errors (4xx family).
     * <p>
     * Typical use cases include:
     * <ul>
     *   <li>Invalid input parameters</li>
     *   <li>Authentication failures</li>
     *   <li>Request validation errors</li>
     * </ul>
     */
    default ClientException toClientException() {
        return new ClientException(this);
    }

    /**
     * Creates a {@link RemoteException} for third-party integration failures.
     * <p>
     * Used when errors originate from:
     * <ul>
     *   <li>Downstream services</li>
     *   <li>External APIs</li>
     *   <li>Infrastructure components</li>
     * </ul>
     */
    default RemoteException toRemoteException() {
        return new RemoteException(this);
    }

    /**
     * Creates a {@link ServiceException} for server-side business errors (5xx family).
     * <p>
     * Indicates problems such as:
     * <ul>
     *   <li>Business rule violations</li>
     *   <li>Data consistency issues</li>
     *   <li>Unrecoverable processing failures</li>
     * </ul>
     */
    default ServiceException toServiceException() {
        return new ServiceException(this);
    }

    /**
     * Identifies the technical origin of the error.
     *
     * @return enum value categorizing where the error originated
     * @see ErrorSourceEnum
     */
    ErrorSourceEnum getErrorSourceEnum();

    /**
     * Specifies the business domain associated with this error.
     *
     * @return enum value representing the affected business area
     * @see BusinessDomainEnum
     */
    BusinessDomainEnum getBusinessDomainEnum();

    /**
     * Provides the unique numeric identifier for this specific error.
     * <p>
     * The number is:
     * <ul>
     *   <li>Guaranteed to be unique within its {@link BusinessDomainEnum}</li>
     *   <li>Formatted as a 3-digit value (001-999)</li>
     *   <li>Statically validated at compile time</li>
     * </ul>
     *
     * @see ErrorNumber
     */
    ErrorNumber getErrorNumber();

    /**
     * Generates the complete error code in standardized format:
     * <p>
     * {@code [Source]-[Domain]-[Number]}
     * <p>
     * Example: {@code CLI-ORDER-101}
     *
     * @return formatted error code string
     */
    default String getCode() {
        return getErrorSourceEnum().getSourceCode() + "-" + getBusinessDomainEnum().name() + "-" + getErrorNumber();
    }

    /**
     * Provides a human-readable explanation of the error.
     * <p>
     * Messages should:
     * <ul>
     *   <li>Be concise but actionable</li>
     *   <li>Avoid technical jargon when possible</li>
     *   <li>Include placeholder values when dynamic data is needed</li>
     * </ul>
     *
     * @return descriptive message suitable for logging and user display
     */
    String getMessage();
}