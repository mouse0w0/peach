package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface PreviewGenerator<T extends Element> {
    void generate(Project project, T element, Path outputFile);
}
