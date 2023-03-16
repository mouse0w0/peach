package com.github.mouse0w0.peach.application;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface AppLifecycleListener {
    Topic<AppLifecycleListener> TOPIC = new Topic<>("AppLifecycleListener", AppLifecycleListener.class, BroadcastDirection.NONE);

    void appStarted();

    void appClosing();

    void canExitApp(MutableBoolean cancelled);

    void appWillBeClosed();
}
