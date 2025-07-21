package com.lcsk42.frameworks.starter.base.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Custom HTTP header constants class
 * <p>
 * This class is used to define custom HTTP header names for various purposes.
 * It is designed to be used in a Spring Cloud Gateway context or similar applications.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomHttpHeaderConstant {

    /**
     * Custom HTTP header for user ID
     */
    public static final String USER_ID = "User-Id";

    /**
     * Custom HTTP header for username
     */
    public static final String REQUEST_ID_HEADER = "Request-Id";

    public static String getClientRequestId(String requestId) {
        return "C-" + requestId;
    }

    public static String getGatewayRequestId() {
        return "G-" + UUID.randomUUID();
    }

    public static String getReturnRequestId() {
        return "R-" + UUID.randomUUID();
    }

    public static String getExceptionRequestId() {
        return "GE-" + UUID.randomUUID();
    }

    /**
     * Custom HTTP header for token
     */
    public static final String TOKEN = "Token";
}
