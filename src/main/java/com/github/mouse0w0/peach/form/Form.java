package com.github.mouse0w0.peach.form;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class Form extends Control {
    private final ObservableList<Group> groups = FXCollections.observableArrayList();

    public Form() {
        getStyleClass().add("form");

        groups.addListener((ListChangeListener<Group>) c -> {
            while (c.next()) {
                ObservableList<? extends Group> list = c.getList();
                if (c.wasRemoved()) {
                    for (Group removed : c.getRemoved()) {
                        removed.formPropertyImpl().set(null);
                    }
                }
                if (c.wasAdded()) {
                    for (int i = c.getFrom(), end = c.getTo(); i < end; i++) {
                        list.get(i).formPropertyImpl().set(Form.this);
                    }
                }
            }
        });
    }

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
    protected Skin<?> createDefaultSkin() {
        return new FormSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Form.class.getResource("Form.css").toExternalForm();
    }
}
