package com.github.mouse0w0.peach.mcmod.element;

import java.nio.file.Path;

public interface ElementCreatedHandler<T extends Element> {
    void onCreated(T element, Path file, String identifier, String name);
}
