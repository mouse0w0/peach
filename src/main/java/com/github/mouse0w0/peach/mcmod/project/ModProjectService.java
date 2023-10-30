package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class ModProjectService implements Disposable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModProjectService.class);

    private final Project project;

    private final Path metadataFile;
    private ModProjectMetadata metadata;

    private final Path resourcesPath;

    public static ModProjectService getInstance(Project project) {
        return project.getService(ModProjectService.class);
    }

    public ModProjectService(Project project) {
        this.project = project;

        this.metadataFile = project.getPath().resolve(ModProjectMetadata.FILE_NAME);

        try {
            this.metadata = JsonUtils.readJson(metadataFile, ModProjectMetadata.class);
        } catch (NoSuchFileException ignored) {
        } catch (IOException e) {
            LOGGER.error("Failed to load metadata.", e);
        }

        if (metadata == null) {
            this.metadata = new ModProjectMetadata();
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

    public ModProjectMetadata getMetadata() {
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
