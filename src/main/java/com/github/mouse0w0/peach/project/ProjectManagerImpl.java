package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.window.WindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProjectManagerImpl implements ProjectManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectManagerImpl.class);

    private final Map<Path, Project> openedProjects = new ConcurrentHashMap<>();

    @Override
    public Collection<Project> getOpenedProjects() {
        return openedProjects.values();
    }

    @Override
    public boolean isProjectOpened(Project project) {
        return getOpenedProjects().contains(project);
    }

    @Override
    @Nullable
    public Project getProject(Path path) {
        for (Path root : openedProjects.keySet()) {
            if (path.startsWith(root)) {
                return openedProjects.get(root);
            }
        }
        return null;
    }

    @Override
    public Project createProject(@Nullable String name, @NotNull Path path) {
        FileUtils.createDirectoriesIfNotExists(path);
        Project project = openProject(path);
        project.setName(name);
        return project;
    }

    @Override
    public Project openProject(@NotNull Path path) {
        if (!path.isAbsolute()) {
            throw new IllegalArgumentException("The path must be absolute, path: " + path);
        }

        Project project = openedProjects.get(path);
        if (project != null) {
            WindowManager.getInstance().getWindow(project).requestFocus();
            return project;
        }

        try {
            project = new ProjectImpl(path);
        } catch (IOException e) {
            LOGGER.error("Failed to open the project.", e);
            // TODO: error report
            throw new Error();
        }
        openedProjects.put(path, project);
        Peach.getInstance().getMessageBus().getPublisher(ProjectLifecycleListener.TOPIC).projectOpened(project);
        LOGGER.info("Opened project: {}", project.getName());
        return project;
    }

    @Override
    public boolean closeProject(@NotNull Project project) {
        if (!project.isOpened()) {
            return true;
        }

        ProjectLifecycleListener publisher = Peach.getInstance().getMessageBus().getPublisher(ProjectLifecycleListener.TOPIC);
        publisher.projectClosingBeforeSave(project);
        try {
            project.save();
        } catch (IOException e) {
            LOGGER.error("Failed to save the project.", e);
            // TODO: error report
        }

        LOGGER.info("Closing project: {}", project.getName());
        publisher.projectClosing(project);

        Disposer.dispose(project);
        openedProjects.remove(project.getPath());

        LOGGER.info("Closed project: {}", project.getName());
        publisher.projectClosed(project);
        return true;
    }

    @Override
    public void closeAllProjects() {
        getOpenedProjects().forEach(this::closeProject);
    }
}
