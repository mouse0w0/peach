package com.github.mouse0w0.peach.windowState;

import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.TypeUtils;
import com.github.mouse0w0.peach.util.Validate;
import com.google.gson.JsonElement;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Storage("windowState.json")
public final class WindowStateServiceImpl implements WindowStateService, PersistentService {
    private static final String STATE_ID = "StateId";
    private static final String LAST_X = "LastX";
    private static final String LAST_Y = "LastY";

    private final EventHandler<WindowEvent> hidden = event -> {
        Window window = (Window) event.getSource();
        getOrCreateState(window).onHidden(window);
    };
    private final ChangeListener<Number> xListener = (observable, oldValue, newValue) -> {
        ReadOnlyDoubleProperty xProperty = (ReadOnlyDoubleProperty) observable;
        Stage stage = (Stage) xProperty.getBean();
        if (isNormal(stage)) {
            stage.getProperties().put(LAST_X, oldValue);
        }
    };
    private final ChangeListener<Number> yListener = (observable, oldValue, newValue) -> {
        ReadOnlyDoubleProperty yProperty = (ReadOnlyDoubleProperty) observable;
        Stage stage = (Stage) yProperty.getBean();
        if (isNormal(stage)) {
            stage.getProperties().put(LAST_Y, oldValue);
        }
    };
    private final InvalidationListener maximizedOrIconifiedListener = observable -> {
        ReadOnlyProperty<?> property = (ReadOnlyProperty<?>) observable;
        Stage stage = (Stage) property.getBean();
        getOrCreateState(stage).onMaximizedOrIconified(stage);
    };

    private static boolean isNormal(Stage stage) {
        return !stage.isMaximized() && !stage.isIconified();
    }

    private final Map<String, WindowState> windowStates = new HashMap<>();

    private WindowState getOrCreateState(Window window) {
        return windowStates.computeIfAbsent((String) window.getProperties().get(STATE_ID), $ -> new WindowState());
    }

    @Override
    public void setup(@NotNull Window window, @NotNull String stateId) {
        Objects.requireNonNull(window);
        Validate.notEmpty(stateId);

        WindowState state = windowStates.get(stateId);
        if (state != null) {
            state.apply(window);
        }

        window.getProperties().put(STATE_ID, stateId);
        window.addEventFilter(WindowEvent.WINDOW_HIDDEN, hidden);
        if (window instanceof Stage stage) {
            window.xProperty().addListener(xListener);
            window.yProperty().addListener(yListener);
            stage.maximizedProperty().addListener(maximizedOrIconifiedListener);
            stage.iconifiedProperty().addListener(maximizedOrIconifiedListener);
        }
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(windowStates);
    }

    @Override
    public void loadState(JsonElement state) {
        windowStates.putAll(JsonUtils.fromJson(state, TypeUtils.map(String.class, WindowState.class)));
    }

    private static final class WindowState {
        private double x;
        private double y;
        private double width;
        private double height;
        private boolean maximized;
        private transient boolean iconified;
        // TODO: full screen?

        public void onHidden(Window window) {
            if (isNormal()) {
                x = window.getX();
                y = window.getY();
                width = window.getWidth();
                height = window.getHeight();
            }
        }

        public void onMaximizedOrIconified(Stage stage) {
            if (isNormal()) {
                ObservableMap<Object, Object> properties = stage.getProperties();
                Double lastX = (Double) properties.get(LAST_X);
                Double lastY = (Double) properties.get(LAST_Y);
                x = lastX != null ? lastX : stage.getX();
                y = lastY != null ? lastY : stage.getY();
                width = stage.getWidth();
                height = stage.getHeight();
            }

            maximized = stage.isMaximized();
            iconified = stage.isIconified();
        }

        private boolean isNormal() {
            return !maximized && !iconified;
        }

        public void apply(Window window) {
            window.setX(x);
            window.setY(y);
            window.setWidth(width);
            window.setHeight(height);

            if (maximized && window instanceof Stage stage) {
                stage.setMaximized(true);
            }
        }
    }
}
