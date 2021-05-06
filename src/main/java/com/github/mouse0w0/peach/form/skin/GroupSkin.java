package com.github.mouse0w0.peach.form.skin;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.form.Group;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class GroupSkin extends TitledPane {
    private final Group group;

    private final GridPane grid;

    public GroupSkin(Group group) {
        this.group = group;

        setFocusTraversable(false);
        setMaxWidth(Double.MAX_VALUE);

        textProperty().bind(group.textProperty());
        collapsibleProperty().bind(group.collapsibleProperty());
        expandedProperty().bindBidirectional(group.expandedProperty());
        visibleProperty().bind(group.visibleProperty());
        managedProperty().bind(group.visibleProperty());
        idProperty().bind(group.idProperty());
        Bindings.bindContent(getStyleClass(), group.getStyleClass());

        grid = new GridPane();
        grid.getStyleClass().setAll("grid");
        grid.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < 12; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100D / 12);
            grid.getColumnConstraints().add(colConst);
        }
        setContent(grid);

        updateElement();
    }

    public Group getGroup() {
        return group;
    }

    private void updateElement() { // low performance
        grid.getChildren().clear();
        int row = 0;
        int column = 0;
        for (Element element : group.getElements()) {
            int colSpan = element.getColSpan().getSpan();
            if (column + colSpan > 12 || column % colSpan != 0) {
                column = 0;
                row++;
            }
            grid.add(new ElementSkin(element), column, row, colSpan, 1);
            column += colSpan;
        }
    }
}
