package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface ElementCreatedHandler<T extends Element> {
    void onCreated(Project project, T element, Path file, String identifier, String name);
}
