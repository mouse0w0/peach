package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.coffeemaker.CoffeeMaker;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.common.collect.Multimap;

import java.nio.file.Path;

public interface Environment {

    Messager getMessager();

    McModMetadata getMetadata();

    Path getSourceDirectory();

    Path getOutputDirectory();

    String getRootPackageName();

    Multimap<ElementType<?>, Element> getElements();

    ModelManager getModelManager();

    CoffeeMaker getCoffeeMaker();

    Filer getClassesFiler();

    Filer getResourcesFiler();

    Filer getAssetsFiler();
}
