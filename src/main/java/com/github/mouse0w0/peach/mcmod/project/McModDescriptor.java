package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Path;

public final class McModDescriptor {
    private final Project project;

    private final JsonFile<McModMetadata> metadata;

    private final Path resourcesPath;

    public static McModDescriptor getInstance(Project project) {
        return project.getService(McModDescriptor.class);
    }

    public McModDescriptor(Project project) {
        this.project = project;

        this.metadata = new JsonFile<>(project.getPath().resolve(McModMetadata.FILE_NAME), McModMetadata.class).load();

        this.resourcesPath = project.getPath().resolve("resources");
    }

    public Project getProject() {
        return project;
    }

    public String getModId() {
        return metadata.get().getId();
    }

    public JsonFile<McModMetadata> getMetadata() {
        return metadata;
    }

    public Path getResourcesPath() {
        return resourcesPath;
    }
}
