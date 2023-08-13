package com.github.mouse0w0.peach.ui.util;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;

public final class GridPanes {
    public static final ColumnConstraints DEFAULT_COLUMN_CONSTRAINTS;
    public static final ColumnConstraints HGROW_COLUMN_CONSTRAINTS;

    static {
        DEFAULT_COLUMN_CONSTRAINTS = new ColumnConstraints();
        HGROW_COLUMN_CONSTRAINTS = new ColumnConstraints();
        HGROW_COLUMN_CONSTRAINTS.setHgrow(Priority.ALWAYS);
    }
}
