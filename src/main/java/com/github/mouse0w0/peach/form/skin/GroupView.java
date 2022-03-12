package com.github.mouse0w0.peach.form.skin;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.form.Group;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class GroupView extends GridPane {

    private final Group group;

    public GroupView(Group group) {
        this.group = group;
        setAlignment(Pos.CENTER_LEFT);
        setMaxWidth(Double.MAX_VALUE);
        getColumnConstraints().setAll(Utils.COLUMN_CONSTRAINTS);
        visibleProperty().bind(group.visibleProperty());
        managedProperty().bind(group.visibleProperty());
        idProperty().bind(group.idProperty());
        Bindings.bindContent(getStyleClass(), group.getStyleClass());

        updateElement();
    }

    private void updateElement() { // low performance
        getChildren().clear();
        int row = 0;
        int column = 0;
        for (Element element : group.getElements()) {
            int colSpan = element.getColSpan().getSpan();
            if (column + colSpan > 12 || column % colSpan != 0) {
                column = 0;
                row++;
            }
            add(new ElementView(element), column, row, colSpan, 1);
            column += colSpan;
        }
    }
}
