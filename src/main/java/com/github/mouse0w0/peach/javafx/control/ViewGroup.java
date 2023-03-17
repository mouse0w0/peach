package com.github.mouse0w0.peach.javafx.control;

import com.github.mouse0w0.peach.javafx.geometry.EightPos;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.SingleSelectionModel;

import java.util.HashMap;

public class ViewGroup {

    public ViewGroup(EightPos pos) {
        this.pos = pos;

        this.selectionModel = new ViewGroupSelectionModel(this);

        getTabs().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends ViewTab> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        for (ViewTab viewTab : c.getRemoved()) {
                            viewTab.setViewGroup(null);
                        }
                    }
                    if (c.wasAdded()) {
                        for (ViewTab viewTab : c.getAddedSubList()) {
                            viewTab.setViewGroup(ViewGroup.this);
                        }
                    }
                }
            }
        });
    }

    private final EightPos pos;

    public final EightPos getPos() {
        return pos;
    }

    private final ObservableList<ViewTab> tabs = FXCollections.observableArrayList();

    public final ObservableList<ViewTab> getTabs() {
        return tabs;
    }

    private final SingleSelectionModel<ViewTab> selectionModel;

    public final SingleSelectionModel<ViewTab> getSelectionModel() {
        return selectionModel;
    }

    private ReadOnlyObjectWrapper<ViewPane> viewPane;

    private ReadOnlyObjectWrapper<ViewPane> viewPanePropertyImpl() {
        if (viewPane == null) {
            viewPane = new ReadOnlyObjectWrapper<>(this, "viewPane");
        }
        return viewPane;
    }

    final void setViewPane(ViewPane viewPane) {
        viewPanePropertyImpl().set(viewPane);
    }

    public final ReadOnlyObjectProperty<ViewPane> viewPaneProperty() {
        return viewPanePropertyImpl().getReadOnlyProperty();
    }

    public final ViewPane getViewPane() {
        return viewPane == null ? null : viewPane.get();
    }

    static class ViewGroupSelectionModel extends SingleSelectionModel<ViewTab> {

        private final ViewGroup viewGroup;

        public ViewGroupSelectionModel(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;

            viewGroup.getTabs().addListener(new ListChangeListener<>() {
                @Override
                public void onChanged(Change<? extends ViewTab> c) {
                    while (c.next()) {
                        if (c.wasRemoved()) {
                            for (ViewTab viewTab : c.getRemoved()) {
                                if (viewTab.isSelected()) clearSelection();
                            }
                        }
                    }
                }
            });
            selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) oldValue.setSelected(false);
                if (newValue != null) newValue.setSelected(true);
            });
        }

        @Override
        protected ViewTab getModelItem(int index) {
            if (index < 0 || index > getItemCount()) return null;
            return viewGroup.getTabs().get(index);
        }

        @Override
        protected int getItemCount() {
            return viewGroup.getTabs().size();
        }
    }

    // Properties
    private ObservableMap<Object, Object> properties;

    public final ObservableMap<Object, Object> getProperties() {
        if (properties == null) {
            properties = FXCollections.observableMap(new HashMap<>());
        }
        return properties;
    }

    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    // UserData
    private static final Object USER_DATA_KEY = new Object();

    public void setUserData(Object value) {
        getProperties().put(USER_DATA_KEY, value);
    }

    public Object getUserData() {
        return getProperties().get(USER_DATA_KEY);
    }
}
