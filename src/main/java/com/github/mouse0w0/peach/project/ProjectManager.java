package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.window.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ProjectManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectManager.class);

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

    @Nullable
    public Project getProject(Path path) {
        for (Path root : openedProjects.keySet()) {
            if (path.startsWith(root)) {
                return openedProjects.get(root);
            }
        }
        return null;
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
            throw new IllegalArgumentException("The path must be absolute, path: " + path);
        }

        Project project = openedProjects.get(path);
        if (project != null) {
            WindowManager.getInstance().getWindow(project).requestFocus();
            return project;
        }

        try {
            project = new Project(path);
        } catch (IOException e) {
            LOGGER.error("Failed to open the project.", e);
            // TODO: error report
            return null;
        }
        openedProjects.put(path, project);
        Peach.getEventBus().post(new ProjectEvent.Opened(project));
        LOGGER.info("Opened project: {}", project.getName());
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
            LOGGER.error("Failed to save the project.", e);
            // TODO: error report
        }

        LOGGER.info("Closing project: {}", project.getName());
        Peach.getEventBus().post(new ProjectEvent.Closing(project));

        project.dispose();
        openedProjects.remove(project.getPath());

        LOGGER.info("Closed project: {}", project.getName());
        Peach.getEventBus().post(new ProjectEvent.Closed(project));
        return true;
    }

    public void closeAllProjects() {
        getOpenedProjects().forEach(this::closeProject);
    }
}
