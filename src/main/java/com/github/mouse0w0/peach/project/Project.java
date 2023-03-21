package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.service.ServiceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;

public interface Project extends ServiceManager {

    @NotNull
    Path getPath();

    @NotNull
    String getName();

    void setName(@Nullable String name);

    boolean isOpened();

    void save() throws IOException;
}
