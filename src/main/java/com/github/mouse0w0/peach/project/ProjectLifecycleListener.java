package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;

public interface ProjectLifecycleListener {
    Topic<ProjectLifecycleListener> TOPIC = new Topic<>("ProjectLifecycleListener", ProjectLifecycleListener.class, BroadcastDirection.NONE);

    default void projectOpened(Project project) {}

    default boolean canCloseProject(Project project) {
        return true;
    }

    default void projectClosingBeforeSave(Project project) {}

    default void projectClosing(Project project) {}

    default void projectClosed(Project project) {}
}
