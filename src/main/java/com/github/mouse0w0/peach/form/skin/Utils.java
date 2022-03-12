package com.github.mouse0w0.peach.form.skin;

import javafx.scene.layout.ColumnConstraints;

import java.util.ArrayList;
import java.util.List;

class Utils {
    public static final double PERCENT_ONE_TWELFTH = 100D / 12D;
    public static final List<ColumnConstraints> COLUMN_CONSTRAINTS;

    static {
        final ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(PERCENT_ONE_TWELFTH);

        COLUMN_CONSTRAINTS = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            COLUMN_CONSTRAINTS.add(colConst);
        }
    }
}
