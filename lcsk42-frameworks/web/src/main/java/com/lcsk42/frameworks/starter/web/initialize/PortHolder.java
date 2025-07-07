package com.lcsk42.frameworks.starter.web.initialize;

import lombok.Getter;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PortHolder implements ApplicationListener<WebServerInitializedEvent> {

    /**
     * The port on which the web server is running.
     * This is set when the application context is initialized.
     */
    private int port;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }
}