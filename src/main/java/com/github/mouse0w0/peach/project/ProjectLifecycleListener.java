package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface ProjectLifecycleListener {
    Topic<ProjectLifecycleListener> TOPIC = new Topic<>("ProjectLifecycleListener", ProjectLifecycleListener.class, BroadcastDirection.NONE);

    void projectOpened(Project project);

    void canCloseProject(Project project, MutableBoolean cancelled);

    void projectClosingBeforeSave(Project project);

    void projectClosing(Project project);

    void projectClosed(Project project);
}
