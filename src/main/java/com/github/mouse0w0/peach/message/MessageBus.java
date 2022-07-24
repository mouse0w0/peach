package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.dispose.Disposable;

import javax.annotation.Nonnull;

public interface MessageBus extends Disposable {
    MessageBus getParent();

    MessageBusConnection connect();

    MessageBusConnection connect(@Nonnull Disposable parentDisposable);

    <T> T getPublisher(@Nonnull Topic<T> topic);
}
