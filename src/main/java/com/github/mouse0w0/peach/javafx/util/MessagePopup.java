package com.github.mouse0w0.peach.javafx.util;

import com.github.mouse0w0.peach.javafx.control.PopupAlert;
import javafx.geometry.Side;
import javafx.scene.Node;

public class MessagePopup {
    private static final PopupAlert POPUP = new PopupAlert();

    public static void show(Node node, String message) {
        POPUP.setText(message);
        POPUP.show(node, Side.TOP, 0, -3);
    }

    public static void hide() {
        POPUP.hide();
    }
}
