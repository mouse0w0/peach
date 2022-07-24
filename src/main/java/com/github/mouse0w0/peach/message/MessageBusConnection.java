package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.dispose.Disposable;

public interface MessageBusConnection extends Disposable {
    <T> void subscribe(Topic<T> topic, T subscriber);

    void disconnect();
}
