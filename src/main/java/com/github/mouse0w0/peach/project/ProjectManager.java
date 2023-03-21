package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    Project createProject(@Nullable String name, @NotNull Path path);

    Project openProject(@NotNull Path path);

    boolean closeProject(@NotNull Project project);

    void closeAllProjects();
}
