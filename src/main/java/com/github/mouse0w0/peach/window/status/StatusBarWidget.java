package com.github.mouse0w0.peach.window.status;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public interface StatusBarWidget {
    @NotNull String getId();

    @NotNull Node getContent();

    void install(@NotNull StatusBar statusBar);
}
