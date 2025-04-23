package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.dispose.Disposable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public interface MessageBus extends Disposable {
    MessageBus getParent();

    MessageBusConnection connect();

    MessageBusConnection connect(@NotNull Disposable parentDisposable);

    <T> T getPublisher(@NotNull Topic<T> topic);

    <T, R> R processSubscribers(@NotNull Topic<T> topic, @NotNull Function<List<T>, R> processor);
}
