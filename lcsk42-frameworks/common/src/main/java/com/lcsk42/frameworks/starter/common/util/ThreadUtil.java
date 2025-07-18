package com.lcsk42.frameworks.starter.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for thread-related operations.
 * Provides helper methods for thread management and control.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadUtil {

    /**
     * Pauses the execution of the current thread for the specified duration.
     * Uses Lombok's @SneakyThrows to silently handle InterruptedException.
     *
     * @param millis the duration to sleep in milliseconds
     */
    @SneakyThrows(value = InterruptedException.class)
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }

    /**
     * Pauses the execution of the current thread for the specified duration in the given time unit.
     * Uses Lombok's @SneakyThrows to silently handle InterruptedException.
     *
     * @param timeout  the duration to sleep
     * @param timeUnit the time unit of the duration
     */
    @SneakyThrows(value = InterruptedException.class)
    public static void sleep(long timeout, TimeUnit timeUnit) {
        timeUnit.sleep(timeout);
    }

    /**
     * Executes a runnable task using the global thread pool.
     * This method is a convenience wrapper around the global thread pool executor.
     *
     * @param runnable the task to be executed
     */
    public static void execute(Runnable runnable) {
        GlobalThreadPool.execute(runnable);
    }

    /**
     * Submits a callable task to the global thread pool and returns a Future representing the result.
     * This method is a convenience wrapper around the global thread pool executor.
     *
     * @param task the callable task to be executed
     * @param <T>  the type of the result returned by the callable
     * @return a Future representing the result of the callable task
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return GlobalThreadPool.submit(task);
    }

    /**
     * Submits a runnable task to the global thread pool and returns a Future representing the execution.
     * This method is a convenience wrapper around the global thread pool executor.
     *
     * @param runnable the runnable task to be executed
     * @return a Future representing the execution of the runnable task
     */
    public static Future<?> submit(Runnable runnable) {
        return GlobalThreadPool.submit(runnable);
    }
}