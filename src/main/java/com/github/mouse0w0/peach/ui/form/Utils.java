package com.github.mouse0w0.peach.ui.form;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;

final class Utils {
    public static final int COLUMN_COUNT = 12;
    public static final double PERCENT_ONE_TWELFTH = 100D / COLUMN_COUNT;
    public static final ColumnConstraints[] COLUMN_CONSTRAINTS;

    static {
        ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(PERCENT_ONE_TWELFTH);
        ColumnConstraints[] colConstArray = new ColumnConstraints[COLUMN_COUNT];
        Arrays.fill(colConstArray, colConst);
        COLUMN_CONSTRAINTS = colConstArray;
    }

    public static void layoutElements(GridPane gridPane, List<Element> elements) {
        gridPane.getChildren().clear();

        int rowOffset = 0, colOffset = 0;
        for (Element element : elements) {
            int colSpan = element.getColSpan();
            if (colOffset > 0 && colOffset + colSpan > COLUMN_COUNT) {
                colOffset = 0;
                rowOffset++;
            }
            gridPane.add(element.getNode(), colOffset, rowOffset, colSpan, 1);
            colOffset += colSpan;
        }
    }
}
