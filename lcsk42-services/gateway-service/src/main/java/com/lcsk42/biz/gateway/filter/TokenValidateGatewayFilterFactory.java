package com.lcsk42.biz.gateway.filter;

import com.lcsk42.biz.gateway.config.GatewayConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

@Component
public class TokenValidateGatewayFilterFactory extends AbstractGatewayFilterFactory<GatewayConfiguration> {

    private static final PathPatternParser pathPatternParser = new PathPatternParser();


    @Override
    public GatewayFilter apply(GatewayConfiguration config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (isRequestAllowed(request, config.getAllowList())) {
                return chain.filter(exchange);
            }

            String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        };
    }

    private boolean isRequestAllowed(ServerHttpRequest request, List<GatewayConfiguration.HttpEndpoint> allowList) {
        HttpMethod requestMethod = request.getMethod();
        String requestPath = request.getPath().toString();
        return allowList.stream().anyMatch(endpoint ->
                isMethodMatch(requestMethod, endpoint.getMethod()) &&
                        isPathMatch(requestPath, endpoint.getPath()));
    }

    private static boolean isMethodMatch(HttpMethod requestMethod, String endpointMethod) {
        if (StringUtils.isBlank(endpointMethod)) {
            return true;
        }
        return requestMethod.name().equalsIgnoreCase(endpointMethod);
    }

    private static boolean isPathMatch(String requestPath, String endpointPath) {
        if (StringUtils.isAnyBlank(requestPath, endpointPath)) {
            return false;
        }
        PathPattern pattern = pathPatternParser.parse(endpointPath);
        return pattern.matches(PathContainer.parsePath(requestPath));
    }
}
