package com.github.mouse0w0.peach.style;

import com.github.mouse0w0.peach.Peach;
import javafx.scene.Parent;
import javafx.scene.Scene;

public interface StyleManager {
    static StyleManager getInstance() {
        return Peach.getInstance().getService(StyleManager.class);
    }

    void reloadUserAgentStylesheet();

    void apply(Scene scene, String styleId);

    void apply(Parent parent, String styleId);
}
