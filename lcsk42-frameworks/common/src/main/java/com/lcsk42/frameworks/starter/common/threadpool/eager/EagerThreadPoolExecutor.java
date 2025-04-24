package com.lcsk42.frameworks.starter.common.threadpool.eager;

import org.springframework.lang.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread pool executor designed for fast task consumption.
 * Tracks the number of submitted tasks and provides enhanced rejection handling.
 */
public class EagerThreadPoolExecutor extends ThreadPoolExecutor {

    // Atomic counter to track the number of submitted tasks
    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    /**
     * Constructs a new EagerThreadPoolExecutor with the given parameters.
     *
     * @param corePoolSize    the number of threads to keep in the pool
     * @param maximumPoolSize the maximum number of threads in the pool
     * @param keepAliveTime   the time excess idle threads will wait for new tasks
     * @param unit            the time unit for keepAliveTime
     * @param workQueue       the queue to hold tasks before they are executed
     * @param threadFactory   the factory to use when creating new threads
     * @param handler         the handler to use when execution is blocked
     */
    public EagerThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime,
                                   TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue,
                                   ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * Returns the current number of submitted tasks.
     *
     * @return the count of submitted tasks
     */
    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }

    /**
     * Hook method called after a task has completed execution.
     * Decrements the submitted task count.
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    /**
     * Executes the given task, incrementing the submitted task count.
     * Provides enhanced rejection handling by attempting to re-offer the task to the queue.
     *
     * @param command the task to execute
     * @throws RejectedExecutionException if the task cannot be accepted for execution
     * @throws NullPointerException       if the command is null
     */
    @Override
    public void execute(@NonNull Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException ex) {
            // Special handling for TaskQueue: try to offer the task again
            TaskQueue taskQueue = (TaskQueue) super.getQueue();
            try {
                if (!taskQueue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", ex);
                }
            } catch (InterruptedException iex) {
                submittedTaskCount.decrementAndGet();
                Thread.currentThread().interrupt();
                throw new RejectedExecutionException(iex);
            }
        } catch (Exception ex) {
            submittedTaskCount.decrementAndGet();
            throw ex;
        }
    }
}
