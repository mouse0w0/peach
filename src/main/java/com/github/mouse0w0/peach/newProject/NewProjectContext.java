package com.github.mouse0w0.peach.newProject;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class NewProjectContext {
    private String projectName;
    private Path projectDirectory;
    private Map<String, Object> properties;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Path getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory(Path projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    public Object getProperty(String key) {
        return properties != null ? properties.get(key) : null;
    }

    public void setProperty(String key, Object value) {
        getProperties().put(key, value);
    }
}
