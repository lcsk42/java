package com.lcsk42.frameworks.starter.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

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
}