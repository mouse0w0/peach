package com.github.mouse0w0.peach.form;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

final class SectionView extends TitledPane {

    public SectionView(Section section) {
        setFocusTraversable(false);
        setMaxWidth(Double.MAX_VALUE);
        textProperty().bind(section.textProperty());
        collapsibleProperty().bind(section.collapsibleProperty());
        expandedProperty().bindBidirectional(section.expandedProperty());
        visibleProperty().bind(section.visibleProperty());
        managedProperty().bind(section.visibleProperty());
        idProperty().bind(section.idProperty());
        Bindings.bindContent(getStyleClass(), section.getStyleClass());

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.getColumnConstraints().setAll(Utils.COLUMN_CONSTRAINTS);
        setContent(grid);

        Utils.layoutElements(grid, section.getElements());
    }
}

