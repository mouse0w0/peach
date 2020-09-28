package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.google.common.collect.Multimap;

import java.nio.file.Path;

public interface Environment {

    McModSettings getModSettings();

    Path getSourceDirectory();

    Path getOutputDirectory();

    String getRootPackageName();

    Multimap<ElementType<?>, Element> getElements();

    ModelManager getModelManager();

    Filer getClassesFiler();

    Filer getResourcesFiler();

    Filer getAssetsFiler();
}
