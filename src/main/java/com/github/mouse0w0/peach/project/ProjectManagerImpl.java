package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.window.WindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProjectManagerImpl implements ProjectManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectManagerImpl.class);

    private final ProjectLifecycleListener publisher = Peach.getInstance().getMessageBus().getPublisher(ProjectLifecycleListener.TOPIC);
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
    public Project createProject(@NotNull Path path, @Nullable String name) {
        Project project = openProject(FileUtils.createDirectories(path));
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
        } catch (Exception e) {
            LOGGER.error("Failed to open project.", e);
            // TODO: error report
            throw new Error();
        }
        openedProjects.put(path, project);
        publisher.projectOpened(project);
        LOGGER.info("Opened project: {}", project.getName());
        return project;
    }

    @Override
    public boolean closeProject(@NotNull Project project) {
        if (!project.isOpened()) {
            return false;
        }

        LOGGER.info("Closing project: {}", project.getName());

        boolean canClose = Peach.getInstance().getMessageBus().processSubscribers(ProjectLifecycleListener.TOPIC,
                listeners -> listeners.stream().allMatch(listener -> listener.canCloseProject(project)));
        if (!canClose) {
            LOGGER.info("Cancelled close project: {}", project.getName());
            return false;
        }

        publisher.projectClosingBeforeSave(project);
        try {
            project.save();
        } catch (Exception e) {
            LOGGER.error("Failed to save project.", e);
            // TODO: error report
        }

        publisher.projectClosing(project);

        Disposer.dispose(project);
        openedProjects.remove(project.getPath());

        LOGGER.info("Closed project: {}", project.getName());
        publisher.projectClosed(project);
        return true;
    }

    @Override
    public void closeAllProjects() {
        for (Project project : getOpenedProjects()) {
            closeProject(project);
        }
    }
}
