package com.lcsk42.biz.admin.common.exception;

import com.lcsk42.frameworks.starter.convention.enums.BusinessDomainEnum;
import com.lcsk42.frameworks.starter.convention.enums.ErrorSourceEnum;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminErrorCode implements ErrorCode {
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
