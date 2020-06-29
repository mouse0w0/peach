package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.util.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectManager {

    private final List<Project> openedProjects = new ArrayList<>();

    public static ProjectManager getInstance() {
        return Peach.getInstance().getService(ProjectManager.class);
    }

    public List<Project> getOpenedProjects() {
        return openedProjects;
    }

    public boolean isProjectOpened(Project project) {
        return openedProjects.contains(project);
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
        try {
            Project project = new Project(path);
            openedProjects.add(project);
            Peach.getEventBus().post(new ProjectEvent.Opened(project));
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean closeProject(@Nonnull Project project) {
        if (!project.isOpen()) {
            return true;
        }

        Peach.getEventBus().post(new ProjectEvent.ClosingBeforeSave(project));
        try {
            project.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Peach.getEventBus().post(new ProjectEvent.Closing(project));
        openedProjects.remove(project);
        Peach.getEventBus().post(new ProjectEvent.Closed(project));
        return true;
    }
}
