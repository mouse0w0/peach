package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class McModDescriptor implements Disposable {
    private static final Logger LOGGER = LoggerFactory.getLogger(McModDescriptor.class);

    private final Project project;

    private final Path metadataFile;
    private McModMetadata metadata;

    private final Path resourcesPath;

    public static McModDescriptor getInstance(Project project) {
        return project.getService(McModDescriptor.class);
    }

    public McModDescriptor(Project project) {
        this.project = project;

        this.metadataFile = project.getPath().resolve(McModMetadata.FILE_NAME);

        if (Files.exists(metadataFile)) {
            try {
                this.metadata = JsonUtils.readJson(metadataFile, McModMetadata.class);
            } catch (IOException e) {
                LOGGER.error("Failed to load metadata.", e);
                this.metadata = new McModMetadata();
            }
        } else {
            this.metadata = new McModMetadata();
        }

        this.resourcesPath = project.getPath().resolve("resources");
    }

    public Project getProject() {
        return project;
    }

    public String getModId() {
        return metadata.getId();
    }

    public Path getMetadataFile() {
        return metadataFile;
    }

    public McModMetadata getMetadata() {
        return metadata;
    }

    public void saveMetadata() {
        try {
            JsonUtils.writeJson(metadataFile, metadata);
        } catch (IOException e) {
            LOGGER.error("Failed to write metadata.", e);
        }
    }

    public Path getResourcesPath() {
        return resourcesPath;
    }

    @Override
    public void dispose() {
        saveMetadata();
    }
}
