package com.lcsk42.frameworks.starter.common.threadpool.proxy;

import com.lcsk42.frameworks.starter.common.util.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for creating proxy wrappers around thread pool rejection handlers.
 * Provides enhanced rejection policy functionality through dynamic proxies.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RejectedProxyUtil {

    /**
     * Creates a proxy wrapper around a rejection policy handler.
     * Enhances the handler with additional capabilities like rejection counting.
     *
     * @param rejectedExecutionHandler The actual rejection policy implementation
     * @param rejectedNum              Counter to track number of rejections
     * @return Proxy-wrapped rejection policy handler
     */
    public static RejectedExecutionHandler createProxy(RejectedExecutionHandler rejectedExecutionHandler, AtomicLong rejectedNum) {
        // Using dynamic proxy pattern to:
        // 1. Enhance thread pool rejection policy behavior
        // 2. Add capabilities like rejection alerts
        // 3. Enable retry mechanisms via delayed queues
        return (RejectedExecutionHandler) Proxy
                .newProxyInstance(
                        rejectedExecutionHandler.getClass().getClassLoader(),
                        new Class[]{RejectedExecutionHandler.class},
                        new RejectedProxyInvocationHandler(rejectedExecutionHandler, rejectedNum));
    }

    /**
     * Test method demonstrating the proxy rejection policy functionality.
     * Creates a thread pool with limited capacity and forces rejections.
     */
    @SuppressWarnings("squid:106")
    public static void main(String[] args) {
        // Create a constrained thread pool (1 core, 3 max, queue size 1)
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 3, 1024, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));

        // Create base AbortPolicy and rejection counter
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        AtomicLong rejectedNum = new AtomicLong();

        // Create enhanced proxy handler
        RejectedExecutionHandler proxyRejectedExecutionHandler = RejectedProxyUtil.createProxy(abortPolicy, rejectedNum);
        threadPoolExecutor.setRejectedExecutionHandler(proxyRejectedExecutionHandler);

        // Submit more tasks than pool can handle to trigger rejections
        for (int i = 0; i < 10; i++) {
            try {
                threadPoolExecutor.execute(() -> ThreadUtil.sleep(100_000L));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        // Print total number of rejections that occurred
        // ("================ Thread pool rejections count: " + rejectedNum.get());
    }
}