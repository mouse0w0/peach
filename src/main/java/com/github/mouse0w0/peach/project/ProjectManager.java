package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectOpenedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectManager {

    private static final ProjectManager INSTANCE = new ProjectManager();

    private final List<Project> openProjects = new ArrayList<>();

    public static ProjectManager getInstance() {
        return INSTANCE;
    }

    public List<Project> getOpenProjects() {
        return openProjects;
    }

    public Project createProject(@Nullable String name, @Nonnull Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            Project project = openProject(path);
            project.setName(name);
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Project openProject(@Nonnull Path path) {
        try {
            Project project = new Project(path);
            Peach.getEventBus().post(new ProjectOpenedEvent(project));
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
