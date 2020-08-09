package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.mcmod.project.McModSettings;

import java.nio.file.Path;

public interface Environment {

    McModSettings getModSettings();

    Path getSourceDirectory();

    Path getOutputDirectory();

    String getRootPackageName();

    Filer getClassesFiler();

    Filer getResourcesFiler();

    Filer getAssetsFiler();
}
