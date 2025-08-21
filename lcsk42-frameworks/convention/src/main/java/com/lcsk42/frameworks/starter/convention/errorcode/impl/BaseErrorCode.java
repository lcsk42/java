package com.lcsk42.frameworks.starter.convention.errorcode.impl;

import com.lcsk42.frameworks.starter.convention.enums.BusinessDomainEnum;
import com.lcsk42.frameworks.starter.convention.enums.ErrorSourceEnum;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BaseErrorCode
 * <p>
 * Defines a standard set of error codes and messages categorized by system layers:
 * - A: Client-side errors (4xx)
 * - B: Service/internal errors (5xx)
 * - C: External/remote service errors
 *
 * <p>Each error code contains:
 * <ul>
 *   <li>Error source (client/service/remote)</li>
 *   <li>Business domain classification</li>
 *   <li>Unique error number within the domain</li>
 *   <li>Human-readable error message</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    /**
     * Generic client-side error (400).
     * <p>Used when client input validation fails or request is malformed.
     */
    CLIENT_ERROR(
            ErrorSourceEnum.CLIENT,
            BusinessDomainEnum.COMMON,
            ErrorNumber.of(1),
            "Client-side error: Invalid request parameters or headers"
    ),
    /**
     * Generic system execution error (500).
     * <p>Used for unexpected internal server errors.
     */
    SERVICE_ERROR(
            ErrorSourceEnum.SERVICE,
            BusinessDomainEnum.COMMON,
            ErrorNumber.of(1),
            "System execution error: Internal server failure"
    ),
    /**
     * Error occurred during Feign client request (502).
     * <p>Used when inter-service communication fails via Feign client.
     */
    SERVICE_FEIGN_ERROR(
            ErrorSourceEnum.SERVICE,
            BusinessDomainEnum.FEIGN,
            ErrorNumber.of(2),
            "Feign request failed: Service unavailable or timeout"
    ),
    /**
     * Failure when calling a third-party remote service (503).
     * <p>Used when external API calls fail (payment gateways, etc.).
     */
    REMOTE_ERROR(
            ErrorSourceEnum.REMOTE,
            BusinessDomainEnum.COMMON,
            ErrorNumber.of(1),
            "Failed to call third-party service: Service unavailable"
    ),
    ;

    /**
     * Source of the error (client, service, or remote).
     */
    private final ErrorSourceEnum errorSourceEnum;
    /**
     * Business domain where the error occurred.
     */
    private final BusinessDomainEnum businessDomainEnum;
    /**
     * Unique error number within the business domain.
     */
    private final ErrorNumber errorNumber;
    /**
     * Human-readable error message.
     * <p>Should provide enough context for troubleshooting while being safe to expose to clients.
     */
    private final String message;
}