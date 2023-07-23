package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.javafx.util.ScrollPanes;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;

final class FormSkin extends SkinBase<Form> {
    private final ScrollPane scrollPane;
    private final VBox container;

    public FormSkin(Form form) {
        super(form);

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        ScrollPanes.fixVerticalScroll(scrollPane);
        getChildren().add(scrollPane);

        container = new VBox();
        container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setContent(container);

        ObservableList<Group> groups = form.getGroups();
        ObservableList<Node> children = container.getChildren();
        for (Group group : groups) {
            children.add(group.getNode());
        }
        groups.addListener((ListChangeListener<Group>) c -> {
            while (c.next()) {
                ObservableList<? extends Group> list = c.getList();
                if (c.wasRemoved()) {
                    for (Group removed : c.getRemoved()) {
                        children.remove(removed.getNode());
                    }
                }
                if (c.wasAdded()) {
                    for (int i = c.getFrom(), end = c.getTo(); i < end; i++) {
                        children.add(i, list.get(i).getNode());
                    }
                }
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(), end = c.getTo(); i < end; i++) {
                        children.set(i, list.get(i).getNode());
                    }
                }
            }
        });
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(scrollPane, contentX, contentY, contentWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
    }
}
