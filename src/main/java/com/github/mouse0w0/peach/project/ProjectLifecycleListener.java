package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface ProjectLifecycleListener {
    Topic<ProjectLifecycleListener> TOPIC = new Topic<>("ProjectLifecycleListener", ProjectLifecycleListener.class, BroadcastDirection.NONE);

    default void projectOpened(Project project) {
    }

    default void canCloseProject(Project project, MutableBoolean cancelled) {
    }

    default void projectClosingBeforeSave(Project project) {
    }

    default void projectClosing(Project project) {
    }

    default void projectClosed(Project project) {
    }
}
