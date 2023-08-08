package com.github.mouse0w0.peach.form;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Form {
    public Form(Group... groups) {
        this();
        getGroups().addAll(groups);
    }

    public Form() {
        getGroups().addListener((ListChangeListener<Group>) c -> {
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

    private final ObservableList<Group> groups = FXCollections.observableArrayList();

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
}
