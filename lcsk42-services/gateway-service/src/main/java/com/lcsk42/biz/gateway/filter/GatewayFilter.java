package com.lcsk42.biz.gateway.filter;

import com.lcsk42.frameworks.starter.base.constant.CustomHttpHeaderConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order
@Component
public class GatewayFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = request.getHeaders()
                .getFirst(CustomHttpHeaderConstant.REQUEST_ID_HEADER);

        if (StringUtils.isBlank(requestId)) {
            requestId = CustomHttpHeaderConstant.getGatewayRequestId();
            request = request.mutate()
                    .header(CustomHttpHeaderConstant.REQUEST_ID_HEADER, requestId)
                    .build();
            exchange = exchange.mutate().request(request).build();
        }

        return chain.filter(exchange);
    }
}
