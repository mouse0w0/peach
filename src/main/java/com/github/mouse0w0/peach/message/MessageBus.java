package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.dispose.Disposable;
import org.jetbrains.annotations.NotNull;

public interface MessageBus extends Disposable {
    MessageBus getParent();

    MessageBusConnection connect();

    MessageBusConnection connect(@NotNull Disposable parentDisposable);

    <T> T getPublisher(@NotNull Topic<T> topic);
}
