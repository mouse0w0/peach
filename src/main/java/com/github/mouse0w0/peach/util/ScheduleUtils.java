package com.github.mouse0w0.peach.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleUtils {
    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
                private AtomicInteger nextId = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "ScheduleThread-" + nextId.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
            });

    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return EXECUTOR.schedule(command, delay, unit);
    }

    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return EXECUTOR.schedule(callable, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return EXECUTOR.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return EXECUTOR.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return EXECUTOR.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result) {
        return EXECUTOR.submit(task, result);
    }

    public static Future<?> submit(Runnable task) {
        return EXECUTOR.submit(task);
    }

    public static <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return EXECUTOR.invokeAll(tasks);
    }

    public static <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return EXECUTOR.invokeAll(tasks, timeout, unit);
    }

    public static <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return EXECUTOR.invokeAny(tasks);
    }

    public static <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return EXECUTOR.invokeAny(tasks, timeout, unit);
    }

    public static void execute(Runnable command) {
        EXECUTOR.execute(command);
    }
}
