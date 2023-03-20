package com.github.mouse0w0.peach.javafx;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public final class TextAreas {
    private static final ListChangeListener<Node> FIX_TEXT_AREA_BLUR_LISTENER = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends Node> c) {
            ScrollPane scrollPane = (ScrollPane) c.getList().get(0);
            scrollPane.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
                while (change.next()) {
                    change.getAddedSubList().forEach(node -> node.setCache(false));
                    change.getRemoved().forEach(node -> node.setCache(true));
                }
            });
            c.getList().removeListener(this);
        }
    };

    /**
     * Fix {@link TextArea} content blur.
     */
    public static void fixTextAreaBlur(TextArea textArea) {
        textArea.getChildrenUnmodifiable().addListener(FIX_TEXT_AREA_BLUR_LISTENER);
    }

    private TextAreas() {
    }
}
