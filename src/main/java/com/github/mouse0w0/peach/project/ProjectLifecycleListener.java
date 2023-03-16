package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;

public interface ProjectLifecycleListener {
    Topic<ProjectLifecycleListener> TOPIC = new Topic<>("ProjectLifecycleListener", ProjectLifecycleListener.class, BroadcastDirection.NONE);

    void projectOpened(Project project);

    void projectClosingBeforeSave(Project project);

    void projectClosing(Project project);

    void projectClosed(Project project);
}
