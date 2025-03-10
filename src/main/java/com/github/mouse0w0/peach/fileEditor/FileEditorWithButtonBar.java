package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.control.ButtonBar;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public abstract class FileEditorWithButtonBar extends BaseFileEditor {
    private final Project project;

    private Node root;

    private Button finish;
    private Button cancel;
    private Button apply;

    public FileEditorWithButtonBar(@NotNull Project project, @NotNull Path file) {
        super(file);
        this.project = Objects.requireNonNull(project);
    }

    protected abstract Node getContent();

    protected boolean validate() {
        return true;
    }

    protected abstract void onApply();

    protected void onCancel() {
        // Nothing to do
    }

    public final void finish() {
        if (validate()) {
            onApply();
            FileEditorManager.getInstance(getProject()).close(getFile());
        }
    }

    public final void cancel() {
        onCancel();
        FileEditorManager.getInstance(getProject()).close(getFile());
    }

    public final void apply() {
        if (validate()) {
            onApply();
        }
    }

    public final Button getFinishButton() {
        return finish;
    }

    public final Button getCancelButton() {
        return cancel;
    }

    public final Button getApplyButton() {
        return apply;
    }

    public final Project getProject() {
        return project;
    }

    @NotNull
    @Override
    public Node getNode() {
        if (root == null) {
            root = createNode();
        }
        return root;
    }

    protected Node createNode() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("file-editor");

        root.setCenter(getContent());

        finish = new Button(AppL10n.localize("button.finish"));
        finish.setDefaultButton(true);
        finish.setOnAction(event -> finish());
        cancel = new Button(AppL10n.localize("button.cancel"));
        cancel.setCancelButton(true);
        cancel.setOnAction(event -> cancel());
        apply = new Button(AppL10n.localize("button.apply"));
        apply.getStyleClass().add("apply");
        apply.setOnAction(event -> apply());

        root.setBottom(new ButtonBar(finish, cancel, apply));

        return root;
    }
}
