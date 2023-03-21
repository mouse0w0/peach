package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.fileEditor.FileEditorWithButtonBar;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public abstract class ElementEditor<T extends Element> extends FileEditorWithButtonBar {
    private final T element;

    public ElementEditor(@NotNull Project project, @NotNull T element) {
        super(project, element.getFile());
        this.element = element;
    }

    public final T getElement() {
        return element;
    }

    protected abstract void initialize(T element);

    protected abstract void updateDataModel(T element);

    @NotNull
    @Override
    public Node getNode() {
        Node node = super.getNode();
        initialize(element);
        return node;
    }

    @Override
    protected void onApply() {
        updateDataModel(element);
        ElementManager.getInstance(getProject()).saveElement(element);
    }
}
