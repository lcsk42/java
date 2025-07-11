package com.lcsk42.frameworks.starter.web;

import com.lcsk42.frameworks.starter.convention.errorcode.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.exception.AbstractException;
import com.lcsk42.frameworks.starter.convention.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Log template for error messages.
     * The log format is [HTTP method] URL [exception] error message.
     */
    private static final String ERROR_LOG_TEMPLATE = "[{}] {} [ex] {}";

    /**
     * Handles parameter validation exceptions (e.g., from @Valid annotations).
     * This will capture errors related to invalid method arguments passed by the client.
     */
    @SneakyThrows
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Void> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        // Extracts the validation error message from the first field error.
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = bindingResult.getFieldErrors().getFirst();
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StringUtils.EMPTY);

        // Logs the error details.
        log.error(ERROR_LOG_TEMPLATE, request.getMethod(), getUrl(request), exceptionStr);

        // Returns a failure result with the error code and message.
        return Result.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
    }

    /**
     * Handles exceptions thrown within the application (i.e., AbstractException).
     * This is used for application-specific exceptions, including custom error handling.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AbstractException.class})
    public Result<Void> abstractException(HttpServletRequest request, AbstractException ex) {
        // Logs the error details, including the exception cause if present.
        if (ex.getCause() != null) {
            log.error(ERROR_LOG_TEMPLATE, request.getMethod(), request.getRequestURL().toString(), ex, ex.getCause());
            return Result.failure(ex);
        }

        // Logs the error details without the cause.
        log.error(ERROR_LOG_TEMPLATE, request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return Result.failure(ex);
    }

    /**
     * Handles uncaught exceptions in the application.
     * This will be triggered for unexpected errors or any other exception not caught explicitly.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Throwable.class)
    public Result<Void> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        // Logs the uncaught exception.
        log.error(ERROR_LOG_TEMPLATE, request.getMethod(), getUrl(request), throwable.toString(), throwable);
        return Result.failure();
    }

    /**
     * Helper method to construct the full URL, including the query string if present.
     */
    private String getUrl(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
