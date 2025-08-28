package com.lcsk42.frameworks.starter.convention.errorcode.impl;

import com.lcsk42.frameworks.starter.convention.enums.BusinessDomainEnum;
import com.lcsk42.frameworks.starter.convention.enums.ErrorSourceEnum;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;
import com.lcsk42.frameworks.starter.convention.errorcode.ErrorNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements ErrorCode {

    IO_RUNTIME_EXCEPTION(
            ErrorSourceEnum.SERVICE,
            BusinessDomainEnum.FILE,
            ErrorNumber.of(1),
            "IO Runtime Exception"
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
