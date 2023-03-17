package com.github.mouse0w0.peach.form;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Form {
    private final ObservableList<Group> groups = FXCollections.observableArrayList();

    public Form() {
        getGroups().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Group> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (Group group : c.getAddedSubList()) {
                            group.formPropertyImpl().set(Form.this);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (Group group : c.getRemoved()) {
                            group.formPropertyImpl().set(null);
                        }
                    }
                }
            }
        });
    }

    public ObservableList<Group> getGroups() {
        return groups;
    }

    public final boolean validate() {
        for (Group group : groups) {
            if (!group.validate()) return false;
        }
        return true;
    }
}
