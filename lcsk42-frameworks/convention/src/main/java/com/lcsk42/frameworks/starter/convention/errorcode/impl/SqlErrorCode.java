package com.lcsk42.frameworks.starter.convention.errorcode.impl;

import com.lcsk42.frameworks.starter.convention.enums.BusinessDomainEnum;
import com.lcsk42.frameworks.starter.convention.enums.ErrorSourceEnum;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SqlErrorCode implements ErrorCode {

    RECORD_NOT_FOUND_EXCEPTION(
            ErrorSourceEnum.CLIENT,
            BusinessDomainEnum.SQL,
            ErrorNumber.of(404),
            "Record not found"
    ),
    DUPLICATE_KEY(
            ErrorSourceEnum.CLIENT,
            BusinessDomainEnum.SQL,
            ErrorNumber.of(409),
            "Duplicate key violation"
    ),
    DATA_INTEGRITY_VIOLATION(
            ErrorSourceEnum.CLIENT,
            BusinessDomainEnum.SQL,
            ErrorNumber.of(422),
            "Data integrity violation"
    ),
    QUERY_TIMEOUT(
            ErrorSourceEnum.CLIENT,
            BusinessDomainEnum.SQL,
            ErrorNumber.of(408),
            "Query timeout"
    ),
    TRANSACTION_ROLLBACK(
            ErrorSourceEnum.CLIENT,
            BusinessDomainEnum.SQL,
            ErrorNumber.of(500),
            "Transaction rollback"
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
