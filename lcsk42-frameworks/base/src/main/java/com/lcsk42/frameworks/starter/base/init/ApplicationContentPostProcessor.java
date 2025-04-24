package com.lcsk42.frameworks.starter.base.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Application Initialization Post Processor,
 * <p>
 * This class listens for the {@link ApplicationReadyEvent}, which is triggered after the Spring Boot application
 * has fully started. It ensures that a custom initialization event {@link ApplicationInitializingEvent} is
 * published exactly once during the application startup phase.
 * </p>
 * <p>
 * The use of {@link AtomicBoolean} guarantees that the event is only triggered once, preventing the event from being
 * fired multiple times in case of repeated invocations or retries.
 * </p>
 */
@RequiredArgsConstructor
public class ApplicationContentPostProcessor implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationContext applicationContext;

    /**
     * Execution flag to ensure the Spring event {@link ApplicationReadyEvent} is executed exactly once.
     * This flag is used to prevent the {@link ApplicationInitializingEvent} from being published more than once
     * during the application lifecycle.
     */
    private final AtomicBoolean executeOnlyOnce = new AtomicBoolean(false);

    /**
     * Handle {@link ApplicationReadyEvent} to trigger a custom initialization event.
     * <p>
     * This method ensures that the event is published only once by using {@link AtomicBoolean}.
     * The {@link ApplicationInitializingEvent} will be fired to notify that the application has completed
     * its initialization phase.
     * </p>
     *
     * @param event the {@link ApplicationReadyEvent} that signals the application has fully started.
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        // Ensure that the event is only published once
        if (!executeOnlyOnce.compareAndSet(false, true)) {
            return;
        }
        // Publish the custom initialization event
        applicationContext.publishEvent(new ApplicationInitializingEvent(this));
    }
}
