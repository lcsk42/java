package com.lcsk42.frameworks.starter.common.threadpool;

import com.lcsk42.frameworks.starter.common.threadpool.eager.EagerThreadPoolExecutor;
import com.lcsk42.frameworks.starter.common.threadpool.eager.TaskQueue;
import com.lcsk42.frameworks.starter.common.util.ThreadUtil;
import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EagerThreadPoolTest {

    private EagerThreadPoolExecutor executor;


    @BeforeEach
    void setUp() {
        TaskQueue taskQueue = new TaskQueue(10);
        executor = new EagerThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS,
                taskQueue,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    void testSubmittedTaskCountIncrementDecrement() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> {
            try {
                latch.await(); // Hold thread to observe count
            } catch (InterruptedException ignored) {
            }
        });

        Thread.sleep(100); // Let task start
        assertEquals(1, executor.getSubmittedTaskCount());

        latch.countDown();
        Thread.sleep(100); // Let task finish
        assertEquals(0, executor.getSubmittedTaskCount());
    }

    @Test
    void testOfferWhenCoreThreadsIdle() {
        AtomicBoolean ran = new AtomicBoolean(false);
        executor.execute(() -> ran.set(true));

        ThreadUtil.sleep(200);
        assertTrue(ran.get(), "Task should execute with core thread.");
    }

    @Test
    void testOfferTriggersThreadCreation() throws InterruptedException {
        // Fill one core thread
        executor.execute(() -> ThreadUtil.sleep(500));

        // Now submittedTaskCount >= pool size, but < max pool size
        // TaskQueue.offer should return false -> trigger new thread
        AtomicBoolean ran = new AtomicBoolean(false);
        executor.execute(() -> ran.set(true));

        Thread.sleep(300);
        assertTrue(ran.get(), "Should trigger new thread due to TaskQueue.offer=false");
    }

    @Test
    @Disabled
    void testOfferFallsBackToQueueWhenPoolFull() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Fill all threads (core + 1 extra)
        executor.execute(() -> ThreadUtil.sleep(300));
        executor.execute(() -> ThreadUtil.sleep(300));

        // This task should be queued
        // Will run after thread becomes free
        executor.execute(latch::countDown);

        // Task should not execute immediately
        assertEquals(3, executor.getSubmittedTaskCount());

        // Wait enough time to allow queued task to run
        latch.await(1, TimeUnit.SECONDS);
        assertEquals(0, executor.getSubmittedTaskCount());
    }

    @Test
    void testRetryOfferSuccessOnRejection() {
        // Create full thread pool
        executor.execute(() -> ThreadUtil.sleep(300));
        executor.execute(() -> ThreadUtil.sleep(300));

        // Override queue behavior to simulate initial offer false, retry true
        TaskQueue retryQueue = new TaskQueue(1) {
            @Override
            public boolean offer(@NonNull Runnable r) {
                return false;
            }

            @Override
            public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) {
                return true; // simulate retry success
            }
        };

        EagerThreadPoolExecutor retryExecutor = new EagerThreadPoolExecutor(
                1, 2, 60, TimeUnit.SECONDS,
                retryQueue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        retryQueue.setExecutor(retryExecutor);

        assertDoesNotThrow(() -> retryExecutor.execute(() -> {
        }));
        retryExecutor.shutdownNow();
    }

    @Test
    void testRetryOfferFailsThrowsRejected() {
        TaskQueue rejectingQueue = new TaskQueue(1) {
            @Override
            public boolean offer(@NonNull Runnable r) {
                return false;
            }

            @Override
            public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) {
                return false; // simulate rejection
            }
        };

        try (EagerThreadPoolExecutor rejectingExecutor = new EagerThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS,
                rejectingQueue,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy())) {


            // Fill all threads
            rejectingExecutor.execute(() -> ThreadUtil.sleep(300));
            rejectingExecutor.execute(() -> ThreadUtil.sleep(300));

            RejectedExecutionException ex = assertThrows(
                    RejectedExecutionException.class,
                    () -> rejectingExecutor.execute(() -> {
                    })
            );

            assertTrue(ex.getMessage().contains("Queue capacity is full"));
            rejectingExecutor.shutdownNow();
        }
    }

    @Test
    void testRetryOfferThrowsInterrupted() {
        TaskQueue interruptedQueue = new TaskQueue(1) {
            @Override
            public boolean offer(@NonNull Runnable r) {
                return false;
            }

            @Override
            public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
                throw new InterruptedException("Simulated interruption");
            }
        };

        EagerThreadPoolExecutor interruptedExecutor = new EagerThreadPoolExecutor(
                1, 2, 60, TimeUnit.SECONDS,
                interruptedQueue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        interruptedQueue.setExecutor(interruptedExecutor);

        // Fill pool
        interruptedExecutor.execute(() -> ThreadUtil.sleep(200));
        interruptedExecutor.execute(() -> ThreadUtil.sleep(200));

        RejectedExecutionException ex = assertThrows(
                RejectedExecutionException.class,
                () -> interruptedExecutor.execute(() -> {
                })
        );
        assertInstanceOf(InterruptedException.class, ex.getCause());
        interruptedExecutor.shutdownNow();
    }

    @Test
    void testRejectAfterShutdown() {
        executor.shutdown();
        assertThrows(RejectedExecutionException.class, () -> executor.execute(() -> {
        }));
    }
}
