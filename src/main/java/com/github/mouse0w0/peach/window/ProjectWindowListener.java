package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;

public interface ProjectWindowListener {
    Topic<ProjectWindowListener> TOPIC = new Topic<>("ProjectWindowListener", ProjectWindowListener.class, BroadcastDirection.TO_PARENT);

    default void windowShown(ProjectWindow window) {}

    default void windowHidden(ProjectWindow window) {}
}
