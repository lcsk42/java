package com.lcsk42.frameworks.starter.common.threadpool;


import com.lcsk42.frameworks.starter.common.threadpool.build.ThreadPoolBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest {

    @Test
    public void createSimpleThreadPool() {
        try (ThreadPoolExecutor threadPool = ThreadPoolBuilder.builder()
                .threadFactory("custom-", false)
                .build()) {
            threadPool.execute(() -> {
                System.out.println("Task executed in custom thread pool");
            });
        }
    }

    public void createThreadPool() {

    }
}
