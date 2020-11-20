package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.wizard.Wizard;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public class WizardFileEditor implements FileEditor {

    private final Path file;
    private final Wizard wizard;

    private String name;
    private Image icon;

    public WizardFileEditor(@Nonnull Path file, @Nonnull Wizard wizard) {
        this.file = Validate.notNull(file);
        this.wizard = Validate.notNull(wizard);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    @Nonnull
    @Override
    public Path getFile() {
        return file;
    }

    @Nonnull
    @Override
    public Node getContent() {
        return wizard.getContent();
    }

    @Override
    public void dispose() {
        wizard.dispose();
    }
}
