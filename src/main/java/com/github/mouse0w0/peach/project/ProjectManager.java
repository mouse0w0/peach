package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.exception.RuntimeIOException;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.util.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {

    private final Map<Path, Project> openedProjects = new HashMap<>();

    public static ProjectManager getInstance() {
        return Peach.getInstance().getService(ProjectManager.class);
    }

    public Collection<Project> getOpenedProjects() {
        return openedProjects.values();
    }

    public boolean isProjectOpened(Project project) {
        return getOpenedProjects().contains(project);
    }

    public Project createProject(@Nullable String name, @Nonnull Path path) {
        try {
            FileUtils.createDirectoriesIfNotExists(path);
            Project project = openProject(path);
            project.setName(name);
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Project openProject(@Nonnull Path path) {
        if (!path.isAbsolute()) {
            throw new IllegalArgumentException("Path must be absolute");
        }

        Project project = openedProjects.get(path);
        if (project != null) {
            WindowManager.getInstance().getWindow(project).requestFocus();
        } else {
            try {
                project = new Project(path);
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
            openedProjects.put(path, project);
            Peach.getEventBus().post(new ProjectEvent.Opened(project));
        }
        return project;
    }

    public boolean closeProject(@Nonnull Project project) {
        if (!project.isOpen()) {
            return true;
        }

        Peach.getEventBus().post(new ProjectEvent.ClosingBeforeSave(project));
        try {
            project.save();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }

        Peach.getEventBus().post(new ProjectEvent.Closing(project));

        project.dispose();
        openedProjects.remove(project.getPath());

        Peach.getEventBus().post(new ProjectEvent.Closed(project));
        return true;
    }

    public void closeAllProjects() {
        getOpenedProjects().forEach(this::closeProject);
    }
}
