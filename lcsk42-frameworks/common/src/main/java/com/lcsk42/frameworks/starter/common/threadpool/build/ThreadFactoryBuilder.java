package com.lcsk42.frameworks.starter.common.threadpool.build;


import com.lcsk42.frameworks.starter.designpattern.builder.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread factory builder that provides flexible thread configuration options.
 * <p>
 * Supports setting thread name prefix, daemon status, priority, and uncaught exception handler.
 * Uses builder pattern to enable method chaining.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadFactoryBuilder implements Builder<ThreadFactory> {

    @Serial
    private static final long serialVersionUID = 1L;

    // Backing thread factory to use (defaults to Executors.defaultThreadFactory)
    private ThreadFactory backingThreadFactory;
    // Prefix for thread names (format: "prefix_counter")
    private String namePrefix;
    // Whether threads should be daemon threads
    private Boolean daemon;
    // Thread priority (1-10)
    private Integer priority;
    // Handler for uncaught exceptions
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * Creates a new ThreadFactoryBuilder instance.
     *
     * @return new ThreadFactoryBuilder instance
     */
    public static ThreadFactoryBuilder builder() {
        return new ThreadFactoryBuilder();
    }

    /**
     * Sets the backing thread factory. If not set, Executors.defaultThreadFactory() will be used.
     *
     * @param backingThreadFactory the backing thread factory
     * @return current builder instance
     * @throws NullPointerException if backingThreadFactory is null
     */
    public ThreadFactoryBuilder threadFactory(@NonNull ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = Objects.requireNonNull(backingThreadFactory,
                "backingThreadFactory cannot be null");
        return this;
    }

    /**
     * Sets the thread name prefix. If set, thread names will follow "prefix_counter" format.
     *
     * @param namePrefix the thread name prefix
     * @return current builder instance
     */
    public ThreadFactoryBuilder prefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    /**
     * Sets whether threads should be daemon threads.
     *
     * @param daemon true for daemon threads, false otherwise
     * @return current builder instance
     */
    public ThreadFactoryBuilder daemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    /**
     * Sets the thread priority.
     *
     * @param priority thread priority (1-10)
     * @return current builder instance
     * @throws IllegalArgumentException if priority is not between Thread.MIN_PRIORITY(1)
     *                                  and Thread.MAX_PRIORITY(10)
     */
    public ThreadFactoryBuilder priority(int priority) {
        if (priority < Thread.MIN_PRIORITY) {
            throw new IllegalArgumentException(String.format("Thread priority (%s) must be >= %s",
                    priority, Thread.MIN_PRIORITY));
        }
        if (priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException(String.format("Thread priority (%s) must be <= %s",
                    priority, Thread.MAX_PRIORITY));
        }
        this.priority = priority;
        return this;
    }

    /**
     * Sets the uncaught exception handler for threads.
     *
     * @param uncaughtExceptionHandler the exception handler
     * @return current builder instance
     */
    public ThreadFactoryBuilder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        return this;
    }

    /**
     * Builds the configured ThreadFactory instance.
     *
     * @return configured ThreadFactory instance
     */
    @Override
    public ThreadFactory build() {
        return build(this);
    }

    /**
     * Internal build method that creates the ThreadFactory based on configuration.
     *
     * @param builder the builder instance
     * @return configured ThreadFactory
     */
    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        // Use configured factory or default if none specified
        final ThreadFactory backingThreadFactory = (null != builder.backingThreadFactory)
                ? builder.backingThreadFactory
                : Executors.defaultThreadFactory();

        final String namePrefix = builder.namePrefix;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler handler = builder.uncaughtExceptionHandler;
        // Counter for thread numbering if name prefix is set
        final AtomicLong count = (null == namePrefix) ? null : new AtomicLong();

        return r -> {
            final Thread thread = backingThreadFactory.newThread(r);
            // Set thread name if prefix configured
            if (null != namePrefix) {
                thread.setName(namePrefix + "-" + count.getAndIncrement());
            }
            // Set daemon status if configured
            if (null != daemon) {
                thread.setDaemon(daemon);
            }
            // Set priority if configured
            if (null != priority) {
                thread.setPriority(priority);
            }
            // Set exception handler if configured
            if (null != handler) {
                thread.setUncaughtExceptionHandler(handler);
            }
            return thread;
        };
    }
}
