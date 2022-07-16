package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public abstract class ElementProvider<T extends Element> {
    private final Class<T> type;
    private final String name;
    private final String translationKey;

    public ElementProvider(Class<T> type, String name) {
        this.type = type;
        this.name = name;
        this.translationKey = "mod.element." + name;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public abstract T newElement(Project project, Path file, String identifier, String name);

    public abstract FileEditor newEditor(Project project, T element);

    public Object[] addIndex(Project project, IndexProvider provider, T element) {
        return null;
    }

    public void removeIndex(Project project, IndexProvider provider, Object[] objects) {
    }
}
