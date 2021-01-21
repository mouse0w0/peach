package com.github.mouse0w0.peach.wm;

import javafx.scene.Node;

import javax.annotation.Nonnull;

public interface StatusBarWidget {

    @Nonnull
    String getId();

    @Nonnull
    Node getContent();

    void install(@Nonnull StatusBar statusBar);
}
