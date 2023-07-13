package com.github.mouse0w0.peach.form;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.GridPane;

final class GroupView extends GridPane {

    public GroupView(Group group) {
        setMaxWidth(Double.MAX_VALUE);
        getColumnConstraints().setAll(Utils.COLUMN_CONSTRAINTS);
        visibleProperty().bind(group.visibleProperty());
        managedProperty().bind(group.visibleProperty());
        idProperty().bind(group.idProperty());
        Bindings.bindContent(getStyleClass(), group.getStyleClass());
        Utils.layoutElements(this, group.getElements());
    }
}
