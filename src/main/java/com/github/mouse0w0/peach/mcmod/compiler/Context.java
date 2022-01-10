package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.common.collect.Multimap;

import java.nio.file.Path;

public interface Context {

    Messager getMessager();

    McModMetadata getMetadata();

    Path getSourceFolder();

    ProjectStructure getProjectStructure();

    Path getOutputFolder();

    Multimap<ElementType<?>, Element> getElements();

    ModelManager getModelManager();

    Filer getClassesFiler();

    Filer getResourcesFiler();

    Filer getAssetsFiler();

    String getRootPackageName();

    String getNamespace();

    String getInternalName(String className);

    String getTranslationKey(String prefix, String identifier);

    String getTranslationKey(String identifier);

    String getResourceKey(String prefix, String identifier);
}
