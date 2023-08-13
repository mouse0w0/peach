package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.DividerType;
import com.github.mouse0w0.peach.ui.control.EightPos;
import javafx.css.PseudoClass;
import javafx.geometry.Orientation;
import javafx.geometry.Side;

class StyleHelper {
    // Side
    public static final PseudoClass TOP = PseudoClass.getPseudoClass("top");
    public static final PseudoClass BOTTOM = PseudoClass.getPseudoClass("bottom");
    public static final PseudoClass LEFT = PseudoClass.getPseudoClass("left");
    public static final PseudoClass RIGHT = PseudoClass.getPseudoClass("right");
    private static final PseudoClass[] SIDE_PSEUDO_CLASSES = {
            TOP, BOTTOM, LEFT, RIGHT
    };

    // Orientation
    public static final PseudoClass VERTICAL = PseudoClass.getPseudoClass("vertical");
    public static final PseudoClass HORIZONTAL = PseudoClass.getPseudoClass("horizontal");

    // EightPos
    public static final PseudoClass TOP_LEFT = PseudoClass.getPseudoClass("top-left");
    public static final PseudoClass TOP_RIGHT = PseudoClass.getPseudoClass("top-right");
    public static final PseudoClass LEFT_TOP = PseudoClass.getPseudoClass("left-top");
    public static final PseudoClass LEFT_BOTTOM = PseudoClass.getPseudoClass("left-right");
    public static final PseudoClass BOTTOM_LEFT = PseudoClass.getPseudoClass("bottom-left");
    public static final PseudoClass BOTTOM_RIGHT = PseudoClass.getPseudoClass("bottom-right");
    public static final PseudoClass RIGHT_TOP = PseudoClass.getPseudoClass("right-top");
    public static final PseudoClass RIGHT_BOTTOM = PseudoClass.getPseudoClass("right-bottom");
    private static final PseudoClass[] EIGHT_POS_PSEUDO_CLASSES = {
            TOP_LEFT, TOP_RIGHT, LEFT_TOP, LEFT_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, RIGHT_TOP, RIGHT_BOTTOM
    };

    // DividerType
    public static final PseudoClass TOP_PRIMARY = PseudoClass.getPseudoClass("top-primary");
    public static final PseudoClass TOP_SECONDARY = PseudoClass.getPseudoClass("top-secondary");
    public static final PseudoClass LEFT_PRIMARY = PseudoClass.getPseudoClass("left-primary");
    public static final PseudoClass LEFT_SECONDARY = PseudoClass.getPseudoClass("left-secondary");
    public static final PseudoClass BOTTOM_PRIMARY = PseudoClass.getPseudoClass("bottom-primary");
    public static final PseudoClass BOTTOM_SECONDARY = PseudoClass.getPseudoClass("bottom-secondary");
    public static final PseudoClass RIGHT_PRIMARY = PseudoClass.getPseudoClass("right-primary");
    public static final PseudoClass RIGHT_SECONDARY = PseudoClass.getPseudoClass("right-secondary");
    private static final PseudoClass[] DIVIDER_TYPE_PSEUDO_CLASSES = {
            TOP_PRIMARY, TOP_SECONDARY, LEFT_PRIMARY, LEFT_SECONDARY, BOTTOM_PRIMARY, BOTTOM_SECONDARY, RIGHT_PRIMARY, RIGHT_SECONDARY
    };

    public static PseudoClass getPseudoClass(Side side) {
        return SIDE_PSEUDO_CLASSES[side.ordinal()];
    }

    public static PseudoClass getPseudoClass(Orientation orientation) {
        return orientation == Orientation.VERTICAL ? VERTICAL : HORIZONTAL;
    }

    public static PseudoClass getPseudoClass(EightPos pos) {
        return EIGHT_POS_PSEUDO_CLASSES[pos.ordinal()];
    }

    public static PseudoClass getPseudoClass(DividerType type) {
        return DIVIDER_TYPE_PSEUDO_CLASSES[type.ordinal()];
    }
}
