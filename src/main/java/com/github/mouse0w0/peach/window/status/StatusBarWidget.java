package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.dispose.Disposable;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public interface StatusBarWidget extends Disposable {
    @NotNull Node getNode();
}
