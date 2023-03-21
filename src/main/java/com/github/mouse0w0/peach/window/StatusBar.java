package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.project.Project;
import org.jetbrains.annotations.NotNull;

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

    void addWidget(@NotNull StatusBarWidget widget);

    void addWidget(@NotNull StatusBarWidget widget, @NotNull Position position);

    void addWidget(@NotNull StatusBarWidget widget, @NotNull Position position, Anchor anchor);

    void addWidget(@NotNull StatusBarWidget widget, Anchor anchor, String anchorId);

    void addWidget(@NotNull StatusBarWidget widget, Position position, Anchor anchor, String anchorId);

    boolean removeWidget(String id);
}
