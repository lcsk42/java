package com.lcsk42.frameworks.starter.web.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lcsk42.frameworks.starter.web.config.WebAutoConfiguration.INITIALIZE_PATH;

@Slf4j(topic = "Initialize DispatcherServlet")
@RestController
public final class InitializeDispatcherServletController {

    /**
     * Endpoint to initialize the DispatcherServlet.
     * This is used to improve the first response time by initializing the servlet early.
     */
    @GetMapping(INITIALIZE_PATH)
    public void initializeDispatcherServlet() {
        // Logs the initialization of the DispatcherServlet.
        log.info("Initialized the dispatcherServlet to improve the first response time of the interface...");
    }
}
