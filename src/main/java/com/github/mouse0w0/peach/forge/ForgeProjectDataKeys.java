package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Path;

public interface ForgeProjectDataKeys {
    Key<JsonFile<ForgeModInfo>> MOD_INFO_FILE = Key.of("ModInfoFile");

    Key<Path> SOURCES_PATH = Key.of("SourcesPath");
    Key<Path> RESOURCES_PATH = Key.of("ResourcesPath");
}
