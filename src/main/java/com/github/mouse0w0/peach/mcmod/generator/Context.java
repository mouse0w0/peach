package com.github.mouse0w0.peach.mcmod.generator;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectMetadata;
import com.google.common.collect.Multimap;

import java.nio.file.Path;
import java.util.Collection;

public interface Context {

    Messager getMessager();

    ModProjectMetadata getMetadata();

    Path getProjectFolder();

    Path getElementFolder();

    Path getResourceFolder();

    Path getModelsFolder();

    Path getTexturesFolder();

    Path getOutputFolder();

    Multimap<Class<?>, Element> getElements();

    <T extends Element> Collection<T> getElements(Class<T> type);

    ModelManager getModelManager();

    Filer getClassesFiler();

    Filer getResourcesFiler();

    Filer getAssetsFiler();

    String getRootPackage();

    String getNamespace();

    String mapIdentifier(Identifier identifier);

    String getInternalName(String className);

    String getTranslationKey(String prefix, String identifier);

    String getTranslationKey(String identifier);

    String getResourceKey(String prefix, String identifier);
}
