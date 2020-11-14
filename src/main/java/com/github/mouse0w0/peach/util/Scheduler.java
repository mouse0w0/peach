package com.github.mouse0w0.peach.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class Scheduler {
    private static final ScheduledExecutorService COMPUTATION_EXECUTOR = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
                private final AtomicInteger nextId = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "Computation Thread - " + nextId.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
            });

    private static final ExecutorService IO_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
        private final AtomicInteger nextId = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "IO Thread - " + nextId.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    });

    public static ScheduledExecutorService computation() {
        return COMPUTATION_EXECUTOR;
    }

    public static ExecutorService io() {
        return IO_EXECUTOR;
    }

    private Scheduler() {
    }
}
