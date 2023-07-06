package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.icon.AppIcon;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class Dialog<R> implements EventTarget {

    private final Stage stage = new Stage();

    private final ObservableList<ButtonType> buttons = FXCollections.observableArrayList();

    private ButtonBar buttonBar;

    private boolean closing;

    public Dialog() {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        getIcons().add(AppIcon.Peach.getImage());
    }

    public Dialog(ButtonType... buttonTypes) {
        this();
        getButtons().addAll(buttonTypes);
    }

    private final ObjectProperty<Callback<ButtonType, R>> resultConverterProperty
            = new SimpleObjectProperty<>(this, "resultConverter");

    public final ObjectProperty<Callback<ButtonType, R>> resultConverterProperty() {
        return resultConverterProperty;
    }

    public final Callback<ButtonType, R> getResultConverter() {
        return resultConverterProperty.get();
    }

    public final void setResultConverter(Callback<ButtonType, R> value) {
        resultConverterProperty.set(value);
    }

    private final ObjectProperty<R> resultProperty = new SimpleObjectProperty<>() {
        protected void invalidated() {
            close();
        }
    };

    public final ObjectProperty<R> resultProperty() {
        return resultProperty;
    }

    public final R getResult() {
        return resultProperty.get();
    }

    public final void setResult(R value) {
        resultProperty.set(value);
    }

    public ObservableList<ButtonType> getButtons() {
        return buttons;
    }

    protected ButtonBar getButtonBar() {
        if (buttonBar == null) {
            buttonBar = createButtonBar();
        }
        return buttonBar;
    }

    protected ButtonBar createButtonBar() {
        final ButtonBar buttonBar = new ButtonBar();
        buttonBar.setMaxWidth(Double.MAX_VALUE);

        addButtonType(buttonBar, getButtons());
        getButtons().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends ButtonType> c) {
                while (c.next()) {
                    addButtonType(buttonBar, c.getAddedSubList());
                    removeButtonType(buttonBar, c.getRemoved());
                }
            }
        });
        return buttonBar;
    }

    private void addButtonType(ButtonBar buttonBar, List<? extends ButtonType> list) {
        ObservableList<Node> buttons = buttonBar.getButtons();
        for (ButtonType buttonType : list) {
            buttons.add(createButton(buttonType));
        }
    }

    private void removeButtonType(ButtonBar buttonBar, List<? extends ButtonType> list) {
        buttonBar.getButtons().removeIf(button -> list.contains(button.getProperties().get(ButtonType.class)));
    }

    protected Node createButton(ButtonType buttonType) {
        Button button = new Button(buttonType.getText());
        ButtonBar.ButtonData buttonData = buttonType.getButtonData();
        ButtonBar.setButtonData(button, buttonData);
        button.setDefaultButton(buttonData.isDefaultButton());
        button.setCancelButton(buttonData.isCancelButton());
        button.getProperties().put(ButtonType.class, buttonType);
        button.addEventHandler(ActionEvent.ACTION, e -> {
            if (e.isConsumed()) return;
            setResultAndClose(buttonType, true);
        });
        return button;
    }

    @SuppressWarnings("unchecked")
    protected void setResultAndClose(ButtonType buttonType, boolean close) {
        Callback<ButtonType, R> resultConverter = getResultConverter();
        if (resultConverter != null) {
            setResult(resultConverter.call(buttonType));
        } else {
            setResult((R) buttonType);
        }

        if (close && isShowing()) {
            close();
        }
    }

    public void setScene(Scene value) {
        stage.setScene(value);
    }

    public void show() {
        stage.show();
    }

    public Optional<R> showAndWait() {
        stage.showAndWait();
        return Optional.ofNullable(getResult());
    }

    public void initStyle(StageStyle style) {
        stage.initStyle(style);
    }

    public StageStyle getStyle() {
        return stage.getStyle();
    }

    public void initModality(Modality modality) {
        stage.initModality(modality);
    }

    public Modality getModality() {
        return stage.getModality();
    }

    public void initOwner(Window owner) {
        stage.initOwner(owner);
    }

    public Window getOwner() {
        return stage.getOwner();
    }

//    public void setFullScreen(boolean value) {
//        stage.setFullScreen(value);
//    }
//
//    public boolean isFullScreen() {
//        return stage.isFullScreen();
//    }
//
//    public ReadOnlyBooleanProperty fullScreenProperty() {
//        return stage.fullScreenProperty();
//    }

    public ObservableList<Image> getIcons() {
        return stage.getIcons();
    }

    public void setTitle(String value) {
        stage.setTitle(value);
    }

    public String getTitle() {
        return stage.getTitle();
    }

    public StringProperty titleProperty() {
        return stage.titleProperty();
    }

    public void setIconified(boolean value) {
        stage.setIconified(value);
    }

    public boolean isIconified() {
        return stage.isIconified();
    }

    public ReadOnlyBooleanProperty iconifiedProperty() {
        return stage.iconifiedProperty();
    }

    public void setMaximized(boolean value) {
        stage.setMaximized(value);
    }

    public boolean isMaximized() {
        return stage.isMaximized();
    }

    public ReadOnlyBooleanProperty maximizedProperty() {
        return stage.maximizedProperty();
    }

    public void setAlwaysOnTop(boolean value) {
        stage.setAlwaysOnTop(value);
    }

    public boolean isAlwaysOnTop() {
        return stage.isAlwaysOnTop();
    }

    public ReadOnlyBooleanProperty alwaysOnTopProperty() {
        return stage.alwaysOnTopProperty();
    }

    public void setResizable(boolean value) {
        stage.setResizable(value);
    }

    public boolean isResizable() {
        return stage.isResizable();
    }

    public BooleanProperty resizableProperty() {
        return stage.resizableProperty();
    }

    public void setMinWidth(double value) {
        stage.setMinWidth(value);
    }

    public double getMinWidth() {
        return stage.getMinWidth();
    }

    public DoubleProperty minWidthProperty() {
        return stage.minWidthProperty();
    }

    public void setMinHeight(double value) {
        stage.setMinHeight(value);
    }

    public double getMinHeight() {
        return stage.getMinHeight();
    }

    public DoubleProperty minHeightProperty() {
        return stage.minHeightProperty();
    }

    public void setMaxWidth(double value) {
        stage.setMaxWidth(value);
    }

    public double getMaxWidth() {
        return stage.getMaxWidth();
    }

    public DoubleProperty maxWidthProperty() {
        return stage.maxWidthProperty();
    }

    public void setMaxHeight(double value) {
        stage.setMaxHeight(value);
    }

    public double getMaxHeight() {
        return stage.getMaxHeight();
    }

    public DoubleProperty maxHeightProperty() {
        return stage.maxHeightProperty();
    }

    public void toFront() {
        stage.toFront();
    }

    public void toBack() {
        stage.toBack();
    }

    public void close() {
        if (closing) return;
        closing = true;

        R result = getResult();

        if (result == null) {
            ButtonType cancelButton = null;
            for (ButtonType button : getButtons()) {
                if (button == null) continue;

                ButtonBar.ButtonData buttonData = button.getButtonData();
                if (buttonData == ButtonBar.ButtonData.CANCEL_CLOSE) {
                    cancelButton = button;
                    break;
                }

                if (buttonData.isCancelButton()) {
                    cancelButton = button;
                }
            }
            setResultAndClose(cancelButton, false);
        }

        stage.hide();

        closing = false;
    }

//    public void setFullScreenExitKeyCombination(KeyCombination keyCombination) {
//        stage.setFullScreenExitKeyCombination(keyCombination);
//    }
//
//    public KeyCombination getFullScreenExitKeyCombination() {
//        return stage.getFullScreenExitKeyCombination();
//    }
//
//    public ObjectProperty<KeyCombination> fullScreenExitKeyProperty() {
//        return stage.fullScreenExitKeyProperty();
//    }
//
//    public void setFullScreenExitHint(String value) {
//        stage.setFullScreenExitHint(value);
//    }
//
//    public String getFullScreenExitHint() {
//        return stage.getFullScreenExitHint();
//    }
//
//    public ObjectProperty<String> fullScreenExitHintProperty() {
//        return stage.fullScreenExitHintProperty();
//    }

    public void sizeToScene() {
        stage.sizeToScene();
    }

    public void centerOnScreen() {
        stage.centerOnScreen();
    }

    public void setX(double value) {
        stage.setX(value);
    }

    public double getX() {
        return stage.getX();
    }

    public ReadOnlyDoubleProperty xProperty() {
        return stage.xProperty();
    }

    public void setY(double value) {
        stage.setY(value);
    }

    public double getY() {
        return stage.getY();
    }

    public ReadOnlyDoubleProperty yProperty() {
        return stage.yProperty();
    }

    public void setWidth(double value) {
        stage.setWidth(value);
    }

    public double getWidth() {
        return stage.getWidth();
    }

    public ReadOnlyDoubleProperty widthProperty() {
        return stage.widthProperty();
    }

    public void setHeight(double value) {
        stage.setHeight(value);
    }

    public double getHeight() {
        return stage.getHeight();
    }

    public ReadOnlyDoubleProperty heightProperty() {
        return stage.heightProperty();
    }

    public void requestFocus() {
        stage.requestFocus();
    }

    public boolean isFocused() {
        return stage.isFocused();
    }

    public ReadOnlyBooleanProperty focusedProperty() {
        return stage.focusedProperty();
    }

    public ObservableMap<Object, Object> getProperties() {
        return stage.getProperties();
    }

    public boolean hasProperties() {
        return stage.hasProperties();
    }

    public void setUserData(Object value) {
        stage.setUserData(value);
    }

    public Object getUserData() {
        return stage.getUserData();
    }

    public Scene getScene() {
        return stage.getScene();
    }

    public ReadOnlyObjectProperty<Scene> sceneProperty() {
        return stage.sceneProperty();
    }

    public void setOpacity(double value) {
        stage.setOpacity(value);
    }

    public double getOpacity() {
        return stage.getOpacity();
    }

    public DoubleProperty opacityProperty() {
        return stage.opacityProperty();
    }

    public void setOnCloseRequest(EventHandler<WindowEvent> value) {
        stage.setOnCloseRequest(value);
    }

    public EventHandler<WindowEvent> getOnCloseRequest() {
        return stage.getOnCloseRequest();
    }

    public ObjectProperty<EventHandler<WindowEvent>> onCloseRequestProperty() {
        return stage.onCloseRequestProperty();
    }

    public void setOnShowing(EventHandler<WindowEvent> value) {
        stage.setOnShowing(value);
    }

    public EventHandler<WindowEvent> getOnShowing() {
        return stage.getOnShowing();
    }

    public ObjectProperty<EventHandler<WindowEvent>> onShowingProperty() {
        return stage.onShowingProperty();
    }

    public void setOnShown(EventHandler<WindowEvent> value) {
        stage.setOnShown(value);
    }

    public EventHandler<WindowEvent> getOnShown() {
        return stage.getOnShown();
    }

    public ObjectProperty<EventHandler<WindowEvent>> onShownProperty() {
        return stage.onShownProperty();
    }

    public void setOnHiding(EventHandler<WindowEvent> value) {
        stage.setOnHiding(value);
    }

    public EventHandler<WindowEvent> getOnHiding() {
        return stage.getOnHiding();
    }

    public ObjectProperty<EventHandler<WindowEvent>> onHidingProperty() {
        return stage.onHidingProperty();
    }

    public void setOnHidden(EventHandler<WindowEvent> value) {
        stage.setOnHidden(value);
    }

    public EventHandler<WindowEvent> getOnHidden() {
        return stage.getOnHidden();
    }

    public ObjectProperty<EventHandler<WindowEvent>> onHiddenProperty() {
        return stage.onHiddenProperty();
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public ReadOnlyBooleanProperty showingProperty() {
        return stage.showingProperty();
    }

    public void hide() {
        close();
    }

    public void setEventDispatcher(EventDispatcher value) {
        stage.setEventDispatcher(value);
    }

    public EventDispatcher getEventDispatcher() {
        return stage.getEventDispatcher();
    }

    public ObjectProperty<EventDispatcher> eventDispatcherProperty() {
        return stage.eventDispatcherProperty();
    }

    public <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        stage.addEventHandler(eventType, eventHandler);
    }

    public <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        stage.removeEventHandler(eventType, eventHandler);
    }

    public <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        stage.addEventFilter(eventType, eventFilter);
    }

    public <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        stage.removeEventFilter(eventType, eventFilter);
    }

    public void fireEvent(Event event) {
        stage.fireEvent(event);
    }

    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return stage.buildEventDispatchChain(tail);
    }
}
