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
                if (c.wasRemoved()) {
                    for (Group removed : c.getRemoved()) {
                        removed.formPropertyImpl().set(null);
                    }
                }
                if (c.wasAdded()) {
                    for (Group added : c.getAddedSubList()) {
                        added.formPropertyImpl().set(Form.this);
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
            if (!group.validate()) {
                return false;
            }
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
