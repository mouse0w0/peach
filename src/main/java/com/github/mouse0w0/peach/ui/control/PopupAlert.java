package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.ui.control.skin.PopupAlertSkin;
import com.sun.javafx.util.Utils;
import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;

public class PopupAlert extends PopupControl {

    public static final PseudoClass WARNING = PseudoClass.getPseudoClass("warning");
    public static final PseudoClass ERROR = PseudoClass.getPseudoClass("error");
    public static final PseudoClass INFO = PseudoClass.getPseudoClass("info");
    public static final PseudoClass SUCCESS = PseudoClass.getPseudoClass("success");

    public enum Type {
        NONE,
        WARNING,
        ERROR,
        INFO,
        SUCCESS
    }

    public PopupAlert() {
        getStyleClass().setAll("popup-alert");

        setAnchorLocation(AnchorLocation.CONTENT_TOP_LEFT);

        bridge = new CSSBridge();
        getContent().setAll(bridge);
    }

    private ObjectProperty<Type> type;

    public final ObjectProperty<Type> typeProperty() {
        if (type == null) {
            type = new SimpleObjectProperty<>(this, "type", Type.NONE);
            type.addListener((observable, oldValue, newValue) -> {
                pseudoClassStateChanged(WARNING, newValue == Type.WARNING);
                pseudoClassStateChanged(ERROR, newValue == Type.ERROR);
                pseudoClassStateChanged(INFO, newValue == Type.INFO);
                pseudoClassStateChanged(SUCCESS, newValue == Type.SUCCESS);
            });
        }
        return type;
    }

    public final Type getType() {
        return type == null ? Type.NONE : type.get();
    }

    public final void setType(Type type) {
        typeProperty().set(type);
    }

    private StringProperty text;

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text", "");
        }
        return text;
    }

    public final String getText() {
        return text == null ? "" : text.getValue();
    }

    public final void setText(String value) {
        textProperty().setValue(value);
    }

    private ObjectProperty<Node> graphic;

    public final ObjectProperty<Node> graphicProperty() {
        if (graphic == null) {
            graphic = new SimpleObjectProperty<>(this, "graphic");
        }
        return graphic;
    }

    public final Node getGraphic() {
        return graphic == null ? null : graphic.getValue();
    }

    public final void setGraphic(Node value) {
        graphicProperty().setValue(value);
    }

    private BooleanProperty closable;

    public final BooleanProperty closableProperty() {
        if (closable == null) {
            closable = new SimpleBooleanProperty(this, "closable", true);
        }
        return closable;
    }

    public final boolean isClosable() {
        return closable == null || closable.get();
    }

    public final void setClosable(boolean closable) {
        closableProperty().set(closable);
    }

    public void show(Node anchor, Side side, double dx, double dy) {
        if (anchor == null) return;

        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());

        HPos hpos = side == Side.LEFT ? HPos.LEFT : side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER;
        VPos vpos = side == Side.TOP ? VPos.TOP : side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER;

        Point2D point = Utils.pointRelativeTo(anchor,
                prefWidth(-1), prefHeight(-1),
                hpos, vpos, dx, dy, true);

        show(anchor, point.getX(), point.getY());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PopupAlertSkin(this);
    }

    private final class CSSBridge extends PopupControl.CSSBridge {
        public CSSBridge() {
            setAccessibleRole(AccessibleRole.TOOLTIP);

            getStyleClass().setAll(PopupAlert.this.getStyleClass());
        }

        @Override
        public String getUserAgentStylesheet() {
            return PopupAlert.class.getResource("PopupAlert.css").toExternalForm();
        }
    }
}
