package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Path;

public interface McModDataKeys {
    Key<JsonFile<McModSettings>> MOD_SETTINGS = Key.of("ModInfoFile");

    Key<Path> RESOURCES_PATH = Key.of("ResourcesPath");
}
