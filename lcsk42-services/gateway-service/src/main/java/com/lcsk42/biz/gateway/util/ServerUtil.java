package com.lcsk42.biz.gateway.util;

import com.lcsk42.frameworks.starter.base.constant.CustomHttpHeaderConstant;
import com.lcsk42.frameworks.starter.common.util.JacksonUtil;
import com.lcsk42.frameworks.starter.convention.result.Result;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServerUtil {

    /**
     * Retrieves the request ID from the ServerHttpRequest headers.
     * If the request ID is not present, it returns a default exception request ID.
     *
     * @param request the ServerHttpRequest from which to retrieve the request ID
     * @return the request ID or a default exception request ID if not found
     */
    public static String getRequestId(ServerHttpRequest request) {
        String requestId = request.getHeaders()
                .getFirst(CustomHttpHeaderConstant.REQUEST_ID_HEADER);

        if (StringUtils.isBlank(requestId)) {
            return CustomHttpHeaderConstant.getExceptionRequestId();
        } else {
            return CustomHttpHeaderConstant.getClientRequestId(requestId);
        }
    }

    /**
     * Writes a JSON response to the given ServerHttpResponse with the specified status code and message.
     *
     * @param response   the ServerHttpResponse to write to
     * @param statusCode the HTTP status code to set
     * @param message    the message to include in the response
     * @param requestId  the request ID to include in the response
     * @return a Mono that completes when the response is written
     */
    public static Mono<Void> write(ServerHttpResponse response, HttpStatusCode statusCode, String message, String requestId) {
        response.setStatusCode(statusCode);

        String json = JacksonUtil.toJSON(
                Result.builder()
                        .code(statusCode.toString())
                        .message(message)
                        .build()
                        .withRequestId(requestId)
        );

        if (StringUtils.isBlank(json)) {
            return response.setComplete();
        }

        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
