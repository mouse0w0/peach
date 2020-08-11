package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.mcmod.element.ElementDefinition;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.google.common.collect.Multimap;

import java.nio.file.Path;

public interface Environment {

    McModSettings getModSettings();

    Path getSourceDirectory();

    Path getOutputDirectory();

    String getRootPackageName();

    Multimap<ElementDefinition<?>, ElementFile<?>> getElements();

    Filer getClassesFiler();

    Filer getResourcesFiler();

    Filer getAssetsFiler();
}
