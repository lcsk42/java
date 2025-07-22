package com.lcsk42.frameworks.starter.convention.exception.sql;

import com.lcsk42.frameworks.starter.convention.errorcode.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SqlErrorCode implements ErrorCode {

    RECORD_NOT_FOUND_EXCEPTION("B-SQL-404", "Record not found"),
    DUPLICATE_KEY("B-SQL-409", "Duplicate key violation"),
    DATA_INTEGRITY_VIOLATION("B-SQL-422", "Data integrity violation"),
    QUERY_TIMEOUT("B-SQL-408", "Query timeout"),
    TRANSACTION_ROLLBACK("B-SQL-500", "Transaction rollback"),
    ;

    /**
     * Error code identifier.
     */
    private final String code;

    /**
     * Human-readable error message.
     */
    private final String message;

    /**
     * Returns the error code.
     *
     * @return the code string.
     */
    @Override
    public String code() {
        return code;
    }

    /**
     * Returns the error message.
     *
     * @return the message string.
     */
    @Override
    public String message() {
        return message;
    }
}
