package com.lcsk42.frameworks.starter.convention.exception.sql;

import com.lcsk42.frameworks.starter.convention.exception.ServiceException;

public class DuplicateKeyException extends ServiceException {
    public DuplicateKeyException() {
        super(SqlErrorCode.DUPLICATE_KEY);
    }
}
