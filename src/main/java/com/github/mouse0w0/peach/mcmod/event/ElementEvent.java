package com.github.mouse0w0.peach.mcmod.event;

import com.github.mouse0w0.eventbus.Event;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public abstract class ElementEvent implements Event {
    private final Project project;
    private final Element element;

    public ElementEvent(Project project, Element element) {
        this.project = project;
        this.element = element;
    }

    public Project getProject() {
        return project;
    }

    public Element getElement() {
        return element;
    }

    public static class Created extends ElementEvent {
        public Created(Project project, Element element) {
            super(project, element);
        }
    }

    public static class Updated extends ElementEvent {
        public Updated(Project project, Element element) {
            super(project, element);
        }
    }

    public static class Deleted implements Event {
        private final Project project;
        private final Path file;

        public Deleted(Project project, Path file) {
            this.project = project;
            this.file = file;
        }

        public Project getProject() {
            return project;
        }

        public Path getFile() {
            return file;
        }
    }
}
