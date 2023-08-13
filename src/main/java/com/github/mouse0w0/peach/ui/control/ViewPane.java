package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.ui.control.skin.ViewPaneSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ViewPane extends Control {

    public ViewPane() {
        getStyleClass().setAll("view-pane");

        getViewGroups().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends com.github.mouse0w0.peach.ui.control.ViewGroup> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (com.github.mouse0w0.peach.ui.control.ViewGroup viewGroup : c.getRemoved()) {
                            viewGroup.setViewPane(null);
                            EightPos pos = viewGroup.getPos();
                            if (getViewGroupImpl(pos) == viewGroup) {
                                cacheViewGroups[pos.ordinal()] = null;
                            }
                        }
                    }
                    if (c.wasAdded()) {
                        for (com.github.mouse0w0.peach.ui.control.ViewGroup viewGroup : c.getAddedSubList()) {
                            viewGroup.setViewPane(ViewPane.this);
                            EightPos pos = viewGroup.getPos();
                            com.github.mouse0w0.peach.ui.control.ViewGroup oldViewGroup = getViewGroupImpl(pos);
                            if (oldViewGroup != null) {
                                getViewGroups().remove(oldViewGroup);
                            }
                            cacheViewGroups[pos.ordinal()] = viewGroup;
                        }
                    }
                }
            }
        });
    }

    private final com.github.mouse0w0.peach.ui.control.ViewGroup[] cacheViewGroups = new com.github.mouse0w0.peach.ui.control.ViewGroup[8];
    private final ObservableList<com.github.mouse0w0.peach.ui.control.ViewGroup> viewGroups = FXCollections.observableArrayList();

    public final ObservableList<com.github.mouse0w0.peach.ui.control.ViewGroup> getViewGroups() {
        return viewGroups;
    }

    public final com.github.mouse0w0.peach.ui.control.ViewGroup getViewGroup(EightPos pos) {
        com.github.mouse0w0.peach.ui.control.ViewGroup viewGroup = getViewGroupImpl(pos);
        if (viewGroup == null) {
            viewGroup = new com.github.mouse0w0.peach.ui.control.ViewGroup(pos);
            getViewGroups().add(viewGroup);
        }
        return viewGroup;
    }

    private ViewGroup getViewGroupImpl(EightPos pos) {
        return cacheViewGroups[pos.ordinal()];
    }

    private ObjectProperty<Node> center;

    public final ObjectProperty<Node> centerProperty() {
        if (center == null) {
            center = new SimpleObjectProperty<>(this, "center");
        }
        return center;
    }

    public final Node getCenter() {
        return center.get();
    }

    public final void setCenter(Node center) {
        centerProperty().set(center);
    }

    private final Divider[] cacheDividers = new Divider[8];
    private final ObservableList<Divider> dividers = FXCollections.observableArrayList();
    private final ObservableList<Divider> unmodifiableDividers = FXCollections.unmodifiableObservableList(dividers);

    public final ObservableList<Divider> getDividers() {
        return unmodifiableDividers;
    }

    public final Divider getDivider(DividerType type) {
        Divider divider = cacheDividers[type.ordinal()];
        if (divider == null) {
            divider = new Divider(type);
            cacheDividers[type.ordinal()] = divider;
            dividers.add(divider);
        }
        return divider;
    }

    public final void setDividerPosition(DividerType type, double position) {
        getDivider(type).setPosition(position);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ViewPane.class.getResource("ViewPane.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ViewPaneSkin(this);
    }

    public static class Divider {
        private final DividerType type;

        public Divider(DividerType type) {
            this.type = type;
        }

        public DividerType getType() {
            return type;
        }

        private DoubleProperty position;

        public final void setPosition(double value) {
            positionProperty().set(value);
        }

        public final double getPosition() {
            return position == null ? computePrefPosition() : position.get();
        }

        public final DoubleProperty positionProperty() {
            if (position == null) {
                position = new SimpleDoubleProperty(this, "position", computePrefPosition());
            }
            return position;
        }

        private double computePrefPosition() {
            return type.isPrimary() ? (type.getSide() == Side.TOP || type.getSide() == Side.LEFT ? 0.2 : 0.8) : 0.5;
        }
    }
}
