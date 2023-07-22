package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.javafx.util.ScrollPanes;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Form extends Region {

    private final ObservableList<Group> groups = FXCollections.observableArrayList();

    private final ScrollPane scrollPane;
    private final VBox content;

    public Form() {
        getStyleClass().add("form");

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        ScrollPanes.fixVerticalScroll(scrollPane);
        getChildren().add(scrollPane);

        content = new VBox();
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setContent(content);

        groups.addListener(groupsListener);
    }

    private final ListChangeListener<Group> groupsListener = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends Group> c) {
            while (c.next()) {
                ObservableList<? extends Group> list = c.getList();
                ObservableList<Node> children = content.getChildren();
                if (c.wasRemoved()) {
                    for (Group removed : c.getRemoved()) {
                        removed.formPropertyImpl().set(null);
                        children.remove(removed.getNode());
                    }
                }
                if (c.wasAdded()) {
                    for (int i = c.getFrom(), end = c.getTo(); i < end; i++) {
                        Group added = list.get(i);
                        added.formPropertyImpl().set(Form.this);
                        children.add(i, added.getNode());
                    }
                }
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(), end = c.getTo(); i < end; i++) {
                        children.set(i, list.get(i).getNode());
                    }
                }
            }
        }
    };

    public final ObservableList<Group> getGroups() {
        return groups;
    }

    public final boolean validate() {
        for (Group group : groups) {
            if (!group.validate()) return false;
        }
        return true;
    }

    @Override
    protected void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
        layoutInArea(scrollPane, x, y, w, h, 0, HPos.LEFT, VPos.TOP);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Form.class.getResource("Form.css").toExternalForm();
    }
}
