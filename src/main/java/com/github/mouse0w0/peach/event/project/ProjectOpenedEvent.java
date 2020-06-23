package com.github.mouse0w0.peach.event.project;

import com.github.mouse0w0.eventbus.Event;
import com.github.mouse0w0.peach.project.Project;

public class ProjectOpenedEvent implements Event {

    private final Project project;

    public ProjectOpenedEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
