package com.github.mouse0w0.peach.mcmod.generator;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.common.collect.Multimap;
import freemarker.template.Configuration;

import java.nio.file.Path;
import java.util.Collection;

public interface Context {

    Messager getMessager();

    McModMetadata getMetadata();

    ProjectStructure getProjectStructure();

    Path getOutputFolder();

    Multimap<ElementType<?>, Element> getElements();

    <T extends Element> Collection<T> getElements(ElementType<T> type);

    ModelManager getModelManager();

    Configuration getTemplateManager();

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
