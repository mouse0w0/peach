package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;

public interface ProjectManager {
    static ProjectManager getInstance() {
        return Peach.getInstance().getService(ProjectManager.class);
    }

    Collection<Project> getOpenedProjects();

    boolean isProjectOpened(Project project);

    @Nullable
    Project getProject(Path path);

    Project createProject(@Nullable String name, @Nonnull Path path);

    Project openProject(@Nonnull Path path);

    boolean closeProject(@Nonnull Project project);

    void closeAllProjects();
}
