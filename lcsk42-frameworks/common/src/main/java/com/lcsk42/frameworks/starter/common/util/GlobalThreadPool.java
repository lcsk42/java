package com.lcsk42.frameworks.starter.common.util;

import com.lcsk42.frameworks.starter.common.threadpool.build.ThreadPoolBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GlobalThreadPool {

    /**
     * Global thread pool executor.
     * This is a singleton instance that can be used throughout the application.
     */
    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * Initializes the global thread pool executor.
     * This method should be called once at application startup to set up the thread pool.
     */
    public static synchronized void init() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdownNow();
        }

        threadPoolExecutor = ThreadPoolBuilder.builder()
                .threadFactory("global-", false)
                .build();
    }

    /**
     * Shuts down the global thread pool executor.
     * If isNow is true, it will attempt to stop all actively executing tasks immediately.
     * Otherwise, it will allow previously submitted tasks to execute before shutting down.
     *
     * @param isNow whether to shut down immediately or gracefully
     */
    public static synchronized void shutdown(boolean isNow) {
        if (null != threadPoolExecutor) {
            if (isNow) {
                threadPoolExecutor.shutdownNow();
            } else {
                threadPoolExecutor.shutdown();
            }
        }

    }

    /**
     * Returns the global thread pool executor.
     * This method can be used to access the thread pool for executing tasks.
     *
     * @return the global thread pool executor
     */
    public static ExecutorService getExecutor() {
        return threadPoolExecutor;
    }

    /**
     * Executes a runnable task in the global thread pool.
     * This method submits the runnable to the thread pool for execution.
     *
     * @param runnable the task to be executed
     */
    public static void execute(@NonNull Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    /**
     * Submits a callable task to the global thread pool.
     * This method returns a Future that can be used to retrieve the result of the task.
     *
     * @param task the callable task to be submitted
     * @param <T>  the type of the result returned by the callable
     * @return a Future representing the pending result of the task
     */
    public static <T> Future<T> submit(@NonNull Callable<T> task) {
        return threadPoolExecutor.submit(task);
    }

    /**
     * Submits a runnable task to the global thread pool.
     * This method returns a Future that can be used to track the execution of the task.
     *
     * @param runnable the runnable task to be submitted
     * @return a Future representing the pending result of the task
     */
    public static Future<?> submit(@NonNull Runnable runnable) {
        return threadPoolExecutor.submit(runnable);
    }

    static {
        init();
    }
}
