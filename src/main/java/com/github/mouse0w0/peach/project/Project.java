package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.service.ServiceManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;

public interface Project extends ServiceManager {

    @Nonnull
    Path getPath();

    @Nonnull
    String getName();

    void setName(@Nullable String name);

    boolean isOpened();

    void save() throws IOException;
}
