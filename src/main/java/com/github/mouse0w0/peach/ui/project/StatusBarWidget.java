package com.github.mouse0w0.peach.ui.project;

import com.sun.istack.internal.NotNull;
import javafx.scene.Node;

public interface StatusBarWidget {

    @NotNull
    String getId();

    @NotNull
    Node getContent();

    void install(@NotNull StatusBar statusBar);
}
