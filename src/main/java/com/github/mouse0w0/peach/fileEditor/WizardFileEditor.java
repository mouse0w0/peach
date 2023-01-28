package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.Validate;
import com.github.mouse0w0.peach.wizard.Wizard;
import javafx.scene.Node;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public class WizardFileEditor extends BaseFileEditor {

    private final Project project;
    private final Wizard wizard;

    public WizardFileEditor(@Nonnull Project project, @Nonnull Path file, @Nonnull Wizard wizard) {
        super(file);
        this.project = Validate.notNull(project);
        this.wizard = Validate.notNull(wizard);
        wizard.addClosedCallback(() -> FileEditorManager.getInstance(project).close(file));
    }

    @Nonnull
    @Override
    public Node getNode() {
        return wizard.getContent();
    }

    @Override
    public void dispose() {
        wizard.cancel();
        wizard.dispose();
    }
}
