package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.javafx.util.ScrollPanes;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FormView extends Region {
    private final ScrollPane scrollPane;
    private final VBox container;

    public FormView() {
        getStyleClass().add("form");

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        ScrollPanes.fixVerticalScroll(scrollPane);
        getChildren().add(scrollPane);

        container = new VBox();
        container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setContent(container);

        ListChangeListener<Group> groupsListener = c -> {
            while (c.next()) {
                var list = c.getList();
                var children = container.getChildren();
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
        };

        formProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getGroups().removeListener(groupsListener);
            }
            if (newValue != null) {
                newValue.getGroups().addListener(groupsListener);
                var children = container.getChildren();
                for (Group group : newValue.getGroups()) {
                    children.add(group.getNode());
                }
            }
        });
    }

    public FormView(Form form) {
        this();
        setForm(form);
    }

    private final ObjectProperty<Form> form = new SimpleObjectProperty<>(this, "form");

    public final ObjectProperty<Form> formProperty() {
        return form;
    }

    public final Form getForm() {
        return form.get();
    }

    public final void setForm(Form form) {
        formProperty().set(form);
    }

    @Override
    protected void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
        layoutInArea(scrollPane, x, y, w, h, 0, HPos.CENTER, VPos.CENTER);
    }


    @Override
    public String getUserAgentStylesheet() {
        return FormView.class.getResource("FormView.css").toExternalForm();
    }
}
