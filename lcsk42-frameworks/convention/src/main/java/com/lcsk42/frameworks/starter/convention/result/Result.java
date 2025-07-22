package com.lcsk42.frameworks.starter.convention.result;

import com.lcsk42.frameworks.starter.convention.errorcode.BaseErrorCode;
import com.lcsk42.frameworks.starter.convention.exception.AbstractException;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Getter
@Builder
@Tag(description = "Response Result", name = "Result")
public class Result<T> implements Serializable {
    /**
     * Constant representing a successful operation.
     */
    public static final String SUCCESS_CODE = "200";

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Response code indicating the status of the result.
     */
    @Schema(description = "Response code indicating the status of the result",
            example = "200")
    private String code;

    /**
     * The actual data returned in the result.
     */
    @SuppressWarnings("squid:S1948")
    @Schema(description = "The actual data returned in the result")
    private T data;

    /**
     * Human-readable message providing more context about the result.
     */
    @Schema(description = "Human-readable message providing more context about the result",
            example = "Operation successful")
    private String message;

    /**
     * Unique identifier for tracing the request.
     */
    @Schema(description = "Unique identifier for tracing the request",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private String requestId;

    /**
     * Constructs a response indicating a successful operation with no additional data.
     *
     * @return a success result without data.
     */
    public static Result<Void> success() {
        return Result.<Void>builder()
                .code(SUCCESS_CODE)
                .build();
    }

    /**
     * Constructs a response indicating a successful operation with provided data.
     *
     * @param data the data to be returned in the result.
     * @return a success result with data.
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(SUCCESS_CODE)
                .data(data)
                .build();
    }

    /**
     * Constructs a default failure response using a predefined service error.
     *
     * @return a failure result with standard error code and message.
     */
    public static Result<Void> failure() {
        return Result.<Void>builder()
                .code(BaseErrorCode.SERVICE_ERROR.code())
                .message(BaseErrorCode.SERVICE_ERROR.message())
                .build();
    }

    /**
     * Constructs a failure response with a custom error message.
     *
     * @param errorMessage the error message to be included in the result.
     * @return a failure result with the provided error message.
     */
    public static Result<Void> failure(String errorMessage) {
        return Result.<Void>builder()
                .code(BaseErrorCode.SERVICE_ERROR.code())
                .message(errorMessage)
                .build();
    }

    /**
     * Constructs a failure response based on a custom platform exception.
     *
     * @param abstractException the exception containing error details.
     * @return a failure result with error code and message from the exception.
     */
    public static Result<Void> failure(AbstractException abstractException) {
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return Result.<Void>builder()
                .code(errorCode)
                .message(errorMessage)
                .build();
    }

    /**
     * Constructs a failure response using a custom error code and message.
     *
     * @param errorCode    the error code.
     * @param errorMessage the error message.
     * @return a failure result with provided error information.
     */
    public static Result<Void> failure(String errorCode, String errorMessage) {
        return Result.<Void>builder()
                .code(errorCode)
                .message(errorMessage)
                .build();
    }

    /**
     * Indicates whether the response is a success.
     *
     * @return true if the response code equals SUCCESS_CODE; false otherwise.
     */
    public boolean isSucceed() {
        return SUCCESS_CODE.equals(code);
    }

    /**
     * Sets the response id.
     *
     * @return the current Result instance for method chaining.
     */
    public Result<T> withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
