package com.github.mouse0w0.peach.form;

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

        int row = 0;
        int column = 0;
        for (Element element : elements) {
            int colSpan = element.getColSpan();
            if (column + colSpan > COLUMN_COUNT) {
                column = 0;
                row++;
            }
            gridPane.add(element.getNode(), column, row, colSpan, 1);
            column += colSpan;
        }
    }
}
