package com.lcsk42.frameworks.starter.convention.exception.sql;

import com.lcsk42.frameworks.starter.convention.exception.ServiceException;

public class TransactionRollbackException extends ServiceException {
    public TransactionRollbackException() {
        super(SqlErrorCode.TRANSACTION_ROLLBACK);
    }
}
