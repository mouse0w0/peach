package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.util.Disposable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public interface FileEditor extends Disposable {

    String getName();

    Image getIcon();

    @Nonnull
    Path getFile();

    @Nonnull
    Node getNode();

    ReadOnlyBooleanProperty modifiedProperty();

    boolean isModified();
}
