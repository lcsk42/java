package com.lcsk42.biz.gateway.handler;

import com.lcsk42.frameworks.starter.base.constant.CustomHttpHeaderConstant;
import com.lcsk42.frameworks.starter.common.util.JacksonUtil;
import com.lcsk42.frameworks.starter.convention.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Slf4j
@Order(-1)
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private record StatusMessage(HttpStatusCode code, String message) {
        public static StatusMessage of(HttpStatusCode code, String message) {
            return new StatusMessage(code, message);
        }
    }

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ServerHttpRequest request = exchange.getRequest();
        String requestId = request.getHeaders()
                .getFirst(CustomHttpHeaderConstant.REQUEST_ID_HEADER);

        if (StringUtils.isBlank(requestId)) {
            requestId = CustomHttpHeaderConstant.getExceptionRequestId();
        }

        StatusMessage statusMessage = switch (ex) {
            case NotFoundException ignored -> {
                log.error("404 Not Found: {}", ex.getMessage());
                yield StatusMessage.of(HttpStatus.NOT_FOUND, "Resource not found");
            }
            case ResponseStatusException statusException -> {
                log.error("Response Status Exception: {}", statusException.getMessage());
                String reason = Optional.ofNullable(statusException.getReason()).orElse("Unknown error");
                yield StatusMessage.of(statusException.getStatusCode(), reason);
            }
            default -> {
                log.error("Internal Server Error: {}", ex.getMessage(), ex);
                yield StatusMessage.of(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
            }
        };

        response.setStatusCode(statusMessage.code);

        String json = JacksonUtil.toJSON(
                Result.builder()
                        .code(statusMessage.code.toString())
                        .message(statusMessage.message)
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