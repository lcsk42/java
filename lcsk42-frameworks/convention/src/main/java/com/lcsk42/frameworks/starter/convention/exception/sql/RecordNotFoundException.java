package com.lcsk42.frameworks.starter.convention.exception.sql;

import com.lcsk42.frameworks.starter.convention.errorcode.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;

public class RecordNotFoundException extends ServiceException {

    /**
     * Constructs a default service exception using {@link BaseErrorCode#SERVICE_ERROR}.
     */
    public RecordNotFoundException() {
        super(SqlErrorCode.RECORD_NOT_FOUND_EXCEPTION);
    }
}
