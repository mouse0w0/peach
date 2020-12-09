package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.fileEditor.BaseFileEditorEx;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Node;

import javax.annotation.Nonnull;

public abstract class ElementEditor<T extends Element> extends BaseFileEditorEx {
    private final T element;

    public ElementEditor(@Nonnull Project project, @Nonnull T element) {
        super(project, element.getFile());
        this.element = element;
    }

    public final T getElement() {
        return element;
    }

    protected abstract void initialize(T element);

    protected abstract void updateDataModel(T element);

    @Nonnull
    @Override
    public Node getNode() {
        Node node = super.getNode();
        initialize(element);
        return node;
    }

    @Override
    protected void onFinished() {
        updateDataModel(element);
        ElementManager.getInstance(getProject()).saveElement(element);
    }
}
