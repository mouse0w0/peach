package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.project.Project;

import javax.annotation.Nonnull;

public interface StatusBar {
    enum Position {
        LEFT, CENTER, RIGHT
    }

    enum Anchor {
        BEFORE, AFTER
    }

    Project getProject();

    StatusBarWidget getWidget(String id);

    boolean hasWidget(String id);

    void addWidget(@Nonnull StatusBarWidget widget);

    void addWidget(@Nonnull StatusBarWidget widget, @Nonnull Position position);

    void addWidget(@Nonnull StatusBarWidget widget, @Nonnull Position position, Anchor anchor);

    void addWidget(@Nonnull StatusBarWidget widget, Anchor anchor, String anchorId);

    void addWidget(@Nonnull StatusBarWidget widget, Position position, Anchor anchor, String anchorId);

    boolean removeWidget(String id);
}
