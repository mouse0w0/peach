package com.github.mouse0w0.peach.ui.util;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class SkinUtils {

    public static <C extends Control> Skin<C> create(C control, Node node) {
        return new SkinImpl<>(control, node);
    }

    private static class SkinImpl<C extends Control> implements Skin<C> {

        private final C skinnable;
        private final Node node;

        private SkinImpl(C skinnable, Node node) {
            this.skinnable = skinnable;
            this.node = node;
        }

        @Override
        public C getSkinnable() {
            return skinnable;
        }

        @Override
        public Node getNode() {
            return node;
        }

        @Override
        public void dispose() {

        }
    }
}
