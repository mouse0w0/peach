package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.icon.Icon;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface FileEditor extends Disposable {

    String getName();

    Icon getIcon();

    @NotNull
    Path getFile();

    @NotNull
    Node getNode();

    ReadOnlyBooleanProperty modifiedProperty();

    boolean isModified();
}
