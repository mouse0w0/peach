package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.project.Project;

public interface ElementEditorFactory<T extends Element> {
    FileEditor create(Project project, T element);
}
