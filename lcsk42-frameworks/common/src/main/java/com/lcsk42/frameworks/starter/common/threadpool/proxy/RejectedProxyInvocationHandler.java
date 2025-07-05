package com.lcsk42.frameworks.starter.common.threadpool.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Proxy handler for thread pool rejection policy execution.
 * Tracks rejection counts and logs errors when rejections occur.
 */
@Slf4j
@AllArgsConstructor
public class RejectedProxyInvocationHandler implements InvocationHandler {

    /**
     * The actual rejection policy implementation being proxied
     */
    private final Object target;

    /**
     * Counter for tracking number of rejections
     */
    private final AtomicLong rejectCount;

    /**
     * Invokes the rejection policy method while tracking rejections and logging errors.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the Method instance corresponding to the interface method
     * @param args   an array of objects containing the method arguments
     * @return the result of the method invocation
     * @throws Throwable if the underlying method throws an exception
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Increment rejection counter
        rejectCount.incrementAndGet();

        try {
            // Log error message about thread pool rejection
            log.error("The thread pool executes the rejection strategy, and the alarm is simulated here...");

            // Invoke the actual rejection policy method
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            // // Unwrap the underlying exception
            throw ex.getCause();
        }
    }
}