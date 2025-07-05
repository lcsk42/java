package com.lcsk42.frameworks.starter.common.threadpool.eager;

import lombok.NonNull;
import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A specialized blocking queue for fast task consumption in thread pools.
 * Works in conjunction with EagerThreadPoolExecutor to optimize thread creation.
 */
@Setter
public class TaskQueue extends LinkedBlockingQueue<Runnable> {
    // Reference to the associated thread pool executor
    private EagerThreadPoolExecutor executor;

    /**
     * Constructs a TaskQueue with the specified capacity.
     *
     * @param capacity the maximum capacity of the queue
     */
    public TaskQueue(int capacity) {
        super(capacity);
    }

    /**
     * Offers a task to the queue with special handling for thread pool optimization.
     * The behavior differs based on current thread pool status:
     * - Prefers queueing when core threads are available
     * - Forces thread creation when below maximum pool size
     * - Falls back to normal queueing when at maximum capacity
     *
     * @param runnable the task to be executed
     * @return true if the task was queued successfully, false otherwise
     * @throws NullPointerException if the runnable is null
     */
    @Override
    public boolean offer(@NonNull Runnable runnable) {
        int currentPoolThreadSize = executor.getPoolSize();

        // When there are idle core threads available,
        // add the task directly to the queue for core threads to process
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }

        // When below maximum pool size, return false to trigger
        // the creation of a new non-core thread (per thread pool logic)
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }

        // At maximum pool size, fall back to normal queue behavior
        return super.offer(runnable);
    }

    /**
     * Attempts to offer a task to the queue with timeout capability.
     * Primarily used for retry operations during task rejection handling.
     *
     * @param o       the task to be executed
     * @param timeout how long to wait before giving up
     * @param unit    the time unit of the timeout argument
     * @return true if successful, false if the specified waiting time elapses
     * @throws InterruptedException       if interrupted while waiting
     * @throws RejectedExecutionException if the executor is shutdown
     */
    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(o, timeout, unit);
    }
}
