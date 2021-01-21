package com.github.mouse0w0.peach.javafx;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

public final class ScrollPanes {

    /**
     * Fix scroll pane without scrolling by the pixel value.
     */
    public static void fixVerticalScroll(ScrollPane scrollPane) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            final double vMin = scrollPane.getVmin();
            final double vMax = scrollPane.getVmax();
            final double vRange = vMax - vMin;
            final Node content = scrollPane.getContent();
            double vPixelValue = 0.0;
            if (content != null) {
                final double height = content.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight();
                if (height > 0.0) vPixelValue = vRange / height;
            }
            final double oldValue = scrollPane.getVvalue();
            final double newValue = oldValue + vPixelValue * -event.getDeltaY();
            if ((event.getDeltaY() > 0.0 && oldValue > vMin) ||
                    (event.getDeltaY() < 0.0 && oldValue < vMax)) {
                scrollPane.setVvalue(newValue);
                event.consume();
            }
        });
    }

    private ScrollPanes() {
    }
}
