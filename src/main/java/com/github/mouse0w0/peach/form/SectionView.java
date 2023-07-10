package com.github.mouse0w0.peach.form;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

final class SectionView extends TitledPane {
    private final Section section;

    private final GridPane grid;

    public SectionView(Section section) {
        this.section = section;
        setFocusTraversable(false);
        setMaxWidth(Double.MAX_VALUE);
        textProperty().bind(section.textProperty());
        collapsibleProperty().bind(section.collapsibleProperty());
        expandedProperty().bindBidirectional(section.expandedProperty());
        visibleProperty().bind(section.visibleProperty());
        managedProperty().bind(section.visibleProperty());
        idProperty().bind(section.idProperty());
        Bindings.bindContent(getStyleClass(), section.getStyleClass());

        grid = new GridPane();
        grid.getStyleClass().setAll("grid");
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.getColumnConstraints().setAll(Utils.COLUMN_CONSTRAINTS);
        setContent(grid);

        updateElement();
    }

    private void updateElement() { // low performance
        grid.getChildren().clear();
        int row = 0;
        int column = 0;
        for (Element element : section.getElements()) {
            int colSpan = element.getColSpan();
            if (column + colSpan > 12 || column % colSpan != 0) {
                column = 0;
                row++;
            }
            grid.add(element.getNode(), column, row, colSpan, 1);
            column += colSpan;
        }
    }
}
