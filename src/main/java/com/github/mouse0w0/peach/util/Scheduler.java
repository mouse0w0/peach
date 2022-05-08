package com.github.mouse0w0.peach.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class Scheduler {
    private static final ScheduledExecutorService INSTANCE = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
                private final AtomicInteger nextId = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "Scheduler Thread-" + nextId.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
            });

    public static ScheduledExecutorService getInstance() {
        return INSTANCE;
    }

    private Scheduler() {
    }
}
