package com.github.mouse0w0.peach.mcmod.compiler;

import java.nio.file.Path;

public final class ProjectStructure {
    private final Path root;

    private final Path sources;

    private final Path resources;
    private final Path blockStates;
    private final Path lang;
    private final Path models;
    private final Path textures;

    public ProjectStructure(Path root) {
        this.root = root;

        this.sources = root.resolve("sources");

        this.resources = root.resolve("resources");
        this.blockStates = root.resolve("blockstates");
        this.lang = root.resolve("lang");
        this.models = root.resolve("models");
        this.textures = root.resolve("textures");
    }

    public Path getRoot() {
        return root;
    }

    public Path getSources() {
        return sources;
    }

    public Path getResources() {
        return resources;
    }

    public Path getBlockStates() {
        return blockStates;
    }

    public Path getLang() {
        return lang;
    }

    public Path getModels() {
        return models;
    }

    public Path getTextures() {
        return textures;
    }
}
