package com.github.mouse0w0.peach.application;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;

public interface AppLifecycleListener {
    Topic<AppLifecycleListener> TOPIC = new Topic<>("AppLifecycleListener", AppLifecycleListener.class, BroadcastDirection.NONE);

    default void appStarted() {}

    default void appClosing() {}

    default boolean canExitApp() {
        return true;
    }

    default void appWillBeClosed() {}
}
