package com.github.mouse0w0.peach.event.project;

import com.github.mouse0w0.eventbus.Event;
import com.github.mouse0w0.peach.project.Project;

public class ProjectEvent implements Event {

    private final Project project;

    public ProjectEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public static final class Opened extends ProjectEvent {
        public Opened(Project project) {
            super(project);
        }
    }

    public static final class Closed extends ProjectEvent {

        public Closed(Project project) {
            super(project);
        }
    }
}
