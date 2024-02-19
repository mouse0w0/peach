package com.github.mouse0w0.peach.application;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface AppLifecycleListener {
    Topic<AppLifecycleListener> TOPIC = new Topic<>("AppLifecycleListener", AppLifecycleListener.class, BroadcastDirection.NONE);

    default void appStarted() {
    }

    default void appClosing() {
    }

    default void canExitApp(MutableBoolean cancelled) {
    }

    default void appWillBeClosed() {
    }
}
