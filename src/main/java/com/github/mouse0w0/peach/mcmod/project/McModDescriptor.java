package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Path;

public final class McModDescriptor {
    private final Project project;

    private final JsonFile<McModSettings> settings;

    private final Path resourcesPath;

    public static McModDescriptor getInstance(Project project) {
        return project.getService(McModDescriptor.class);
    }

    public McModDescriptor(Project project) {
        this.project = project;

        this.settings = new JsonFile<>(project.getPath().resolve(McModSettings.FILE_NAME), McModSettings.class).load();

        this.resourcesPath = project.getPath().resolve("resources");
    }

    public Project getProject() {
        return project;
    }

    public JsonFile<McModSettings> getSettings() {
        return settings;
    }

    public Path getResourcesPath() {
        return resourcesPath;
    }
}
