package com.github.mouse0w0.peach.form.skin;

import com.github.mouse0w0.peach.form.Form;
import com.github.mouse0w0.peach.form.FormView;
import com.github.mouse0w0.peach.form.Group;
import com.github.mouse0w0.peach.javafx.ScrollPanes;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;

public class FormViewSkin extends SkinBase<FormView> {

    private final ScrollPane scrollPane;
    private final VBox content;

    private final ListChangeListener<Group> groupsListener = new ListChangeListener<Group>() {
        @Override
        public void onChanged(Change<? extends Group> c) {
            while (c.next()) {
                ObservableList<? extends Group> list = c.getList();
                ObservableList<Node> children = content.getChildren();
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
        }
    };

    public FormViewSkin(FormView formView) {
        super(formView);

        consumeMouseEvents(false);

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        ScrollPanes.fixVerticalScroll(scrollPane);
        getChildren().add(scrollPane);

        content = new VBox();
        content.getStyleClass().setAll("group-box");
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setContent(content);

        formView.formProperty().addListener(observable -> updateForm());
        updateForm();
    }

    private void updateForm() {
        ObservableList<Node> children = content.getChildren();
        children.clear();

        Form form = getSkinnable().getForm();
        if (form != null) {
            ObservableList<Group> groups = form.getGroups();
            groups.addListener(new WeakListChangeListener<>(groupsListener));
            for (Group group : groups) {
                children.add(group.getNode());
            }
        }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + scrollPane.minWidth(height) + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + scrollPane.minHeight(width) + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + scrollPane.prefWidth(height) + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + scrollPane.prefHeight(width) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(scrollPane, contentX, contentY, contentWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
    }
}
