package com.github.mouse0w0.peach.javafx.control;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.ContextMenuEvent;

import java.util.HashMap;

public class ViewTab {

    public static final Object VIEW_TAB_KEY = new Object();

    public static ViewTab getViewTab(Node content) {
        if (content != null && content.hasProperties()) {
            return (ViewTab) content.getProperties().get(VIEW_TAB_KEY);
        }
        return null;
    }

    public ViewTab() {
        this(null, null, null);
    }

    public ViewTab(String text) {
        this(text, null, null);
    }

    public ViewTab(Node graphic) {
        this(null, graphic, null);
    }

    public ViewTab(String text, Node content) {
        this(text, null, content);
    }

    public ViewTab(Node graphic, Node content) {
        this(null, graphic, content);
    }

    public ViewTab(String text, Node graphic, Node content) {
        if (text != null) setText(text);
        if (graphic != null) setGraphic(graphic);
        if (content != null) setContent(content);
    }

    private ReadOnlyObjectWrapper<com.github.mouse0w0.peach.javafx.control.ViewGroup> viewGroup;

    private ReadOnlyObjectWrapper<com.github.mouse0w0.peach.javafx.control.ViewGroup> viewGroupPropertyImpl() {
        if (viewGroup == null) {
            viewGroup = new ReadOnlyObjectWrapper<>(this, "viewGroup");
        }
        return viewGroup;
    }

    final void setViewGroup(com.github.mouse0w0.peach.javafx.control.ViewGroup viewGroup) {
        viewGroupPropertyImpl().set(viewGroup);
    }

    public final ReadOnlyObjectProperty<com.github.mouse0w0.peach.javafx.control.ViewGroup> viewGroupProperty() {
        return viewGroupPropertyImpl().getReadOnlyProperty();
    }

    public final com.github.mouse0w0.peach.javafx.control.ViewGroup getViewGroup() {
        return viewGroup == null ? null : viewGroup.get();
    }

    private StringProperty text;

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public final String getText() {
        return text == null ? null : text.get();
    }

    public final void setText(String value) {
        textProperty().set(value);
    }

    private ObjectProperty<Node> graphic;

    public final ObjectProperty<Node> graphicProperty() {
        if (graphic == null) {
            graphic = new SimpleObjectProperty<>(this, "graphic");
        }
        return graphic;
    }

    public final Node getGraphic() {
        return graphic == null ? null : graphic.get();
    }

    public final void setGraphic(Node value) {
        graphicProperty().set(value);
    }

    private ObjectProperty<Node> content;

    public final ObjectProperty<Node> contentProperty() {
        if (content == null) {
            content = new SimpleObjectProperty<Node>(this, "content") {
                @Override
                public void set(Node newValue) {
                    Node oldValue = get();
                    if (oldValue != null) {
                        oldValue.getProperties().remove(VIEW_TAB_KEY);
                    }

                    super.set(newValue);

                    if (newValue != null) {
                        newValue.getProperties().put(VIEW_TAB_KEY, ViewTab.this);
                    }
                }
            };
        }
        return content;
    }

    public final Node getContent() {
        return content == null ? null : content.get();
    }

    public final void setContent(Node value) {
        contentProperty().set(value);
    }

    private ObjectProperty<ContextMenu> contextMenu;

    public final ObjectProperty<ContextMenu> contextMenuProperty() {
        if (contextMenu == null) {
            contextMenu = new SimpleObjectProperty<>(this, "contextMenu");
        }
        return contextMenu;
    }

    public final ContextMenu getContextMenu() {
        return contextMenu == null ? null : contextMenu.get();
    }

    public final void setContextMenu(ContextMenu value) {
        contextMenuProperty().set(value);
    }

    public ObjectProperty<EventHandler<? super ContextMenuEvent>> onContextMenuRequested;

    public final ObjectProperty<EventHandler<? super ContextMenuEvent>> onContextMenuRequestedProperty() {
        if (onContextMenuRequested == null) {
            onContextMenuRequested = new SimpleObjectProperty<>(this, "onContextMenuRequested");
        }
        return onContextMenuRequested;
    }

    public final EventHandler<? super ContextMenuEvent> getOnContextMenuRequested() {
        return onContextMenuRequested == null ? null : onContextMenuRequested.get();
    }

    public final void setOnContextMenuRequested(EventHandler<? super ContextMenuEvent> onContextMenuRequested) {
        onContextMenuRequestedProperty().set(onContextMenuRequested);
    }

    private BooleanProperty selected;

    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new SimpleBooleanProperty(this, "selected") {
                @Override
                protected void invalidated() {
                    ViewGroup viewGroup = getViewGroup();
                    if (viewGroup != null) {
                        SingleSelectionModel<ViewTab> selectionModel = viewGroup.getSelectionModel();
                        if (isSelected()) {
                            selectionModel.select(ViewTab.this);
                        } else if (selectionModel.getSelectedItem() == ViewTab.this) {
                            selectionModel.clearSelection();
                        }
                    }
                }
            };
        }
        return selected;
    }

    public final boolean isSelected() {
        return selected != null && selected.get();
    }

    public final void setSelected(boolean selected) {
        selectedProperty().set(selected);
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
