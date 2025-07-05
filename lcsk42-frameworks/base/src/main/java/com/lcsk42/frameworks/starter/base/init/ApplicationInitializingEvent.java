package com.lcsk42.frameworks.starter.base.init;

import org.springframework.context.ApplicationEvent;

/**
 * Application Initialization Event
 *
 * <p>
 * A specialized event published during the initialization phase of the application.
 * <br>
 * This event provides a standardized mechanism for observing and handling
 * custom initialization behaviors across the business system.
 * </p>
 *
 * <p>
 * You can listen to this event using Spring's {@code @EventListener} annotation
 * to trigger specific setup tasks or perform additional configuration.
 * </p>
 */
public class ApplicationInitializingEvent extends ApplicationEvent {

    /**
     * Construct a new {@code ApplicationInitializingEvent}.
     *
     * @param source the object on which the event initially occurred,
     *               or with which the event is associated (never {@code null}).
     *               Typically, this would be the Spring application context.
     */
    public ApplicationInitializingEvent(Object source) {
        super(source);
    }
}
