package com.github.mouse0w0.peach.service.store;

import com.github.mouse0w0.peach.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ProjectServiceStore extends ServiceStoreBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceStore.class);

    public static final String STORE_FOLDER = ".peach";

    public static final String NAME_FILE = ".name";

    private final Project project;

    private String projectName;

    public ProjectServiceStore(Project project) {
        super(project.getPath().resolve(STORE_FOLDER));
        this.project = project;
    }

    public String getProjectName() {
        if (projectName == null) {
            Path nameFile = getStorePath().resolve(NAME_FILE);
            if (Files.exists(nameFile)) {
                try {
                    List<String> lines = Files.readAllLines(nameFile);
                    projectName = lines.isEmpty() ? null : lines.get(0);
                } catch (IOException e) {
                    LOGGER.error("Failed to load project name file.", e);
                    projectName = null;
                }
            }
        }
        return projectName;
    }

    public void setProjectName(String name) {
        Path nameFile = getStorePath().resolve(NAME_FILE);
        try {
            if (name == null || name.equals(project.getPath().getFileName().toString())) {
                Files.deleteIfExists(nameFile);
                projectName = null;
            } else {
                Files.writeString(nameFile, name, StandardOpenOption.CREATE);
                projectName = name;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save project name file.", e);
        }
    }
}
