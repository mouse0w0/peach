package com.github.mouse0w0.peach.event.project;

import com.github.mouse0w0.peach.ui.project.ProjectWindow;

public class ProjectWindowEvent extends ProjectEvent {
    private final ProjectWindow window;

    protected ProjectWindowEvent(ProjectWindow window) {
        super(window.getProject());
        this.window = window;
    }

    public ProjectWindow getWindow() {
        return window;
    }

    public static final class Opened extends ProjectWindowEvent {
        public Opened(ProjectWindow window) {
            super(window);
        }
    }

    public static final class Closed extends ProjectWindowEvent {
        public Closed(ProjectWindow window) {
            super(window);
        }
    }
}
