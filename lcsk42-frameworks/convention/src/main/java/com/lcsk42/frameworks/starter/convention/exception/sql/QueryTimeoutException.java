package com.lcsk42.frameworks.starter.convention.exception.sql;

import com.lcsk42.frameworks.starter.convention.exception.ServiceException;

public class QueryTimeoutException extends ServiceException {
    public QueryTimeoutException() {
        super(SqlErrorCode.QUERY_TIMEOUT);
    }
}
