package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.project.Project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public abstract class BaseFileEditorEx extends BaseFileEditor {
    private final Project project;

    private final BorderPane root;

    private final Button finish;
    private final Button cancel;
    private final Button apply;

    public BaseFileEditorEx(@Nonnull Project project, @Nonnull Path file) {
        super(file);
        this.project = Validate.notNull(project);

        root = new BorderPane();
        root.setPadding(new Insets(9));
        root.getStyleClass().add("file-editor");

        root.setCenter(getContent());

        finish = new Button(I18n.translate("dialog.button.finish"));
        finish.setDefaultButton(true);
        finish.setOnAction(event -> finish());
        cancel = new Button(I18n.translate("dialog.button.cancel"));
        cancel.setCancelButton(true);
        cancel.setOnAction(event -> cancel());
        apply = new Button(I18n.translate("dialog.button.apply"));
        apply.getStyleClass().add("apply");
        apply.setOnAction(event -> apply());

        HBox buttonBar = new HBox(9, finish, cancel, apply);
        buttonBar.getStyleClass().add("button-bar");
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        root.setBottom(buttonBar);
    }

    protected abstract Node getContent();

    protected boolean validate() {
        return true;
    }

    protected abstract void onFinished();

    protected void onCancelled() {
        // Nothing to do
    }

    @Override
    public void dispose() {
        // Nothing to do
    }

    public final void finish() {
        if (validate()) {
            onFinished();
            FileEditorManager.getInstance(getProject()).close(getFile());
        }
    }

    public final void cancel() {
        onCancelled();
        FileEditorManager.getInstance(getProject()).close(getFile());
    }

    public final void apply() {
        if (validate()) {
            onFinished();
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

    @Nonnull
    @Override
    public final Node getNode() {
        return root;
    }
}
