package com.lcsk42.biz.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = GatewayConfiguration.PREFIX)
public class GatewayConfiguration {

    public static final String PREFIX = "framework.gateway";

    /**
     * Paths that do not require authentication
     */
    private List<HttpEndpoint> allowList;

    @Data
    public static class HttpEndpoint {
        /**
         * HTTP method
         */
        private String method;

        /**
         * Path for the HTTP endpoint
         * /info, /info/, /info/**, etc.
         */
        private String path;
    }
}
