package com.github.mouse0w0.peach.ui.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class Container extends Region {
    public Container() {
    }

    public Container(Node content) {
        setContent(content);
    }

    private final ObjectProperty<Node> content = new ObjectPropertyBase<>() {
        @Override
        public Object getBean() {
            return Container.this;
        }

        @Override
        public String getName() {
            return "content";
        }

        @Override
        protected void invalidated() {
            Node content = get();
            if (content == null) {
                getChildren().clear();
            } else {
                getChildren().setAll(content);
            }
        }
    };

    public final ObjectProperty<Node> contentProperty() {
        return content;
    }

    public final Node getContent() {
        return content.get();
    }

    public final void setContent(Node value) {
        content.set(value);
    }

    @Override
    protected void layoutChildren() {
        Node content = getContent();
        if (content != null) {
            double x = snappedLeftInset();
            double y = snappedTopInset();
            double w = snapSizeX(getWidth()) - x - snappedRightInset();
            double h = snapSizeY(getHeight()) - y - snappedBottomInset();
            if (content.isResizable()) {
                content.resize(w, h);
            }
            content.relocate(x, y);
        }
    }
}
