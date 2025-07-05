package com.lcsk42.frameworks.starter.common.threadpool.build;


import com.lcsk42.frameworks.starter.designpattern.builder.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Thread pool builder that provides a fluent API for configuring and creating ThreadPoolExecutor instances.
 * This is an immutable builder and is thread-safe.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadPoolBuilder implements Builder<ThreadPoolExecutor> {

    // Default core pool size is 5x CPU cores (based on 20% utilization calculation)
    private int corePoolSize = calculateCoreNum();

    // Default maximum pool size is 1.5x core pool size
    private int maximumPoolSize = corePoolSize + (corePoolSize >> 1);

    // Default thread keep-alive time is 30 seconds
    private long keepAliveTime = 30 * 1_000L;

    // Default time unit is milliseconds
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    // Default work queue is unbounded with capacity 4096
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(4096);

    // Default rejection policy is AbortPolicy
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    // Default thread type is non-daemon
    private boolean isDaemon = false;

    // Thread name prefix
    private String threadNamePrefix;

    // Thread factory
    private ThreadFactory threadFactory;

    /**
     * Factory method to create a ThreadPoolBuilder instance.
     *
     * @return new ThreadPoolBuilder instance
     */
    public static ThreadPoolBuilder builder() {
        return new ThreadPoolBuilder();
    }

    /**
     * Calculates default core thread count based on CPU cores and 20% utilization.
     *
     * @return calculated core thread count
     */
    private int calculateCoreNum() {
        int cpuCoreNum = Runtime.getRuntime().availableProcessors();
        return new BigDecimal(cpuCoreNum).divide(new BigDecimal("0.2"), RoundingMode.HALF_UP).intValue();
    }

    /**
     * Sets a custom thread factory.
     *
     * @param threadFactory the thread factory instance
     * @return current builder instance (for method chaining)
     */
    public ThreadPoolBuilder threadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    /**
     * Sets the core pool size.
     *
     * @param corePoolSize number of core threads
     * @return current builder instance (for method chaining)
     * @throws IllegalArgumentException if corePoolSize is negative
     */
    public ThreadPoolBuilder corePoolSize(int corePoolSize) {
        if (corePoolSize < 0) {
            throw new IllegalArgumentException("Core pool size must be non-negative");
        }
        this.corePoolSize = corePoolSize;
        return this;
    }

    /**
     * Sets the maximum pool size. If smaller than current core pool size,
     * automatically adjusts core pool size down.
     *
     * @param maximumPoolSize maximum number of threads
     * @return current builder instance (for method chaining)
     * @throws IllegalArgumentException if maximumPoolSize is <=0 or less than core pool size
     */
    public ThreadPoolBuilder maximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize <= 0) {
            throw new IllegalArgumentException("Maximum pool size must be positive");
        }
        this.maximumPoolSize = maximumPoolSize;
        if (maximumPoolSize < this.corePoolSize) {
            this.corePoolSize = maximumPoolSize;
        }
        return this;
    }

    /**
     * Sets basic properties for the thread factory.
     *
     * @param threadNamePrefix prefix for thread names
     * @param isDaemon         whether threads should be daemon threads
     * @return current builder instance (for method chaining)
     */
    public ThreadPoolBuilder threadFactory(String threadNamePrefix, Boolean isDaemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.isDaemon = isDaemon;
        return this;
    }

    /**
     * Sets thread keep-alive time (using default time unit milliseconds).
     *
     * @param keepAliveTime keep-alive duration
     * @return current builder instance (for method chaining)
     */
    public ThreadPoolBuilder keepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    /**
     * Sets thread keep-alive time with specified time unit.
     *
     * @param keepAliveTime keep-alive duration
     * @param timeUnit      time unit for duration
     * @return current builder instance (for method chaining)
     */
    public ThreadPoolBuilder keepAliveTime(long keepAliveTime, TimeUnit timeUnit) {
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        return this;
    }

    /**
     * Sets the rejection policy handler.
     *
     * @param rejectedExecutionHandler rejection policy handler
     * @return current builder instance (for method chaining)
     */
    public ThreadPoolBuilder rejected(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        return this;
    }

    /**
     * Sets the work queue implementation.
     *
     * @param workQueue work queue instance
     * @return current builder instance (for method chaining)
     */
    public ThreadPoolBuilder workQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    /**
     * Builds the ThreadPoolExecutor instance.
     *
     * @return configured ThreadPoolExecutor instance
     * @throws IllegalArgumentException if parameters are invalid or thread name prefix is empty
     */
    @Override
    public ThreadPoolExecutor build() {
        if (threadFactory == null) {
            Assert.hasLength(threadNamePrefix, "The thread name prefix cannot be empty or an empty string.");
            threadFactory = ThreadFactoryBuilder.builder().prefix(threadNamePrefix).daemon(isDaemon).build();
        }
        ThreadPoolExecutor executorService;
        try {
            executorService = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    workQueue,
                    threadFactory,
                    rejectedExecutionHandler);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error creating thread pool parameter.", ex);
        }
        return executorService;
    }
}
