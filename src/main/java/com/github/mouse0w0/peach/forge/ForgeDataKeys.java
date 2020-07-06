package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Path;

public interface ForgeDataKeys {
    Key<JsonFile<ForgeModInfo>> MOD_INFO_FILE = Key.of("MOD_INFO_FILE");

    Key<Path> SOURCES_PATH = Key.of("SOURCES_PATH");
    Key<Path> RESOURCES_PATH = Key.of("RESOURCES_PATH");
}
