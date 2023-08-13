package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.peach.ui.control.Tip;
import javafx.geometry.Side;
import javafx.scene.Node;

public class MessagePopup {
    private static final Tip POPUP = new Tip();

    public static void show(Node node, String message) {
        POPUP.setText(message);
        POPUP.show(node, Side.TOP, 0, -3);
    }

    public static void hide() {
        POPUP.hide();
    }
}
