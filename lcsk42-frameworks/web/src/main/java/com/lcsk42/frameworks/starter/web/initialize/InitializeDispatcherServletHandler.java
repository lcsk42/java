package com.lcsk42.frameworks.starter.web.initialize;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static com.lcsk42.frameworks.starter.web.config.WebAutoConfiguration.INITIALIZE_PATH;

@RequiredArgsConstructor
public final class InitializeDispatcherServletHandler implements CommandLineRunner {

    // RestTemplate for making HTTP requests
    private final RestTemplate restTemplate;

    // Port holder to retrieve the server port
    private final PortHolder portHolder;

    // Configurable environment to fetch application properties
    private final ConfigurableEnvironment configurableEnvironment;

    /**
     * This method is executed on application startup.
     * It triggers a GET request to initialize the DispatcherServlet
     * in order to improve the response time of the first request.
     */
    @Override
    public void run(String... args) throws Exception {
        // Constructs the URL to call the initialization endpoint
        String url = String.format("http://127.0.0.1:%s%s",
                portHolder.getPort() +
                        configurableEnvironment.getProperty("server.servlet.context-path", "") +
                        "/api",
                INITIALIZE_PATH);

        try {
            // Executes the GET request to the initialization endpoint
            restTemplate.execute(url, HttpMethod.GET, null, null);
        } catch (Throwable ignored) {
            // If an error occurs (e.g., service is unavailable), it is silently ignored
        }
    }
}
