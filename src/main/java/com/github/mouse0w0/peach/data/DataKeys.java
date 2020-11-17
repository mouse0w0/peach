package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface DataKeys {
    DataKey<Project> PROJECT = DataKey.create("Project");

    DataKey<Path> FILE_PATH = DataKey.create("FilePath");
}
