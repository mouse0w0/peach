package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.javafx.util.ScrollPanes;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FormView extends Region {

    private ObjectProperty<Form> form;

    private final ScrollPane scrollPane;
    private final VBox content;

    public FormView() {
        getStyleClass().add("form-view");

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        ScrollPanes.fixVerticalScroll(scrollPane);
        getChildren().add(scrollPane);

        content = new VBox();
        content.getStyleClass().setAll("group-box");
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setContent(content);

        formProperty().addListener(this::formChanged);
    }

    private final ListChangeListener<Group> groupsListener = new ListChangeListener<>() {
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

    private void formChanged(Observable observable, Form oldValue, Form newValue) {
        if (oldValue != null) {
            oldValue.getGroups().removeListener(groupsListener);
        }

        ObservableList<Node> children = content.getChildren();
        children.clear();

        if (newValue != null) {
            ObservableList<Group> groups = newValue.getGroups();
            for (Group group : groups) {
                children.add(group.getNode());
            }
            groups.addListener(groupsListener);
        }
    }

    public FormView(Form form) {
        this();
        setForm(form);
    }

    public ObjectProperty<Form> formProperty() {
        if (form == null) {
            form = new SimpleObjectProperty<>(this, "form");
        }
        return form;
    }

    public Form getForm() {
        return form != null ? form.get() : null;
    }

    public void setForm(Form form) {
        formProperty().set(form);
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
        return FormView.class.getResource("FormView.css").toExternalForm();
    }
}
