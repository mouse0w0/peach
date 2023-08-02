package com.github.mouse0w0.peach.windowState;

import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.Validate;
import com.google.gson.JsonElement;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Storage("windowState.json")
public class WindowStateServiceImpl implements WindowStateService, PersistentService {
    private static final String STATE_ID = "StateId";
    private static final String LAST_X = "LastX";
    private static final String LAST_Y = "LastY";

    private final EventHandler<WindowEvent> hidden = event -> {
        Window window = (Window) event.getSource();
        String stateId = getStateId(window);
        getOrCreateWindowState(stateId).store(window);
    };
    private final ChangeListener<Number> xListener = (observable, oldValue, newValue) -> {
        ReadOnlyDoubleProperty xProperty = (ReadOnlyDoubleProperty) observable;
        Stage stage = (Stage) xProperty.getBean();
        if (!stage.isMaximized()) {
            stage.getProperties().put(LAST_X, oldValue);
        }
    };
    private final ChangeListener<Number> yListener = (observable, oldValue, newValue) -> {
        ReadOnlyDoubleProperty yProperty = (ReadOnlyDoubleProperty) observable;
        Stage stage = (Stage) yProperty.getBean();
        if (!stage.isMaximized()) {
            stage.getProperties().put(LAST_Y, oldValue);
        }
    };
    private final InvalidationListener maximizedListener = (observable) -> {
        ReadOnlyBooleanProperty maximizedProperty = (ReadOnlyBooleanProperty) observable;
        Window window = (Window) maximizedProperty.getBean();
        String stateId = getStateId(window);
        if (maximizedProperty.get()) {
            getOrCreateWindowState(stateId).storeWhenMaximized(window);
        } else {
            WindowState state = getWindowState(stateId);
            if (state != null) state.applyLocationAndSize(window);
        }
    };

    private static String getStateId(Window window) {
        return (String) window.getProperties().get(STATE_ID);
    }

    private final Map<String, WindowState> windowStates = new HashMap<>();

    private WindowState getWindowState(String stateId) {
        return windowStates.get(stateId);
    }

    private WindowState getOrCreateWindowState(String stateId) {
        return windowStates.computeIfAbsent(stateId, k -> new WindowState());
    }

    @Override
    public void register(@NotNull Window window, @NotNull String stateId) {
        Validate.notNull(window);
        Validate.notEmpty(stateId);

        WindowState state = getWindowState(stateId);
        if (state != null) {
            state.apply(window);
        }

        window.getProperties().put(STATE_ID, stateId);
        window.addEventFilter(WindowEvent.WINDOW_HIDDEN, hidden);
        if (window instanceof Stage) {
            window.xProperty().addListener(xListener);
            window.yProperty().addListener(yListener);
            ((Stage) window).maximizedProperty().addListener(maximizedListener);
        }
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(windowStates);
    }

    @Override
    public void loadState(JsonElement jsonElement) {
        windowStates.putAll(JsonUtils.fromJson(jsonElement, TypeUtils.parameterize(Map.class, String.class, WindowState.class)));
    }

    private static final class WindowState {
        private double x;
        private double y;
        private double width;
        private double height;
        private boolean maximized;

        public void store(Window window) {
            if (window instanceof Stage) {
                maximized = ((Stage) window).isMaximized();
            }
            if (!maximized) {
                x = window.getX();
                y = window.getY();
                width = window.getWidth();
                height = window.getHeight();
            }
        }

        public void storeWhenMaximized(Window window) {
            ObservableMap<Object, Object> properties = window.getProperties();
            Double lastX = (Double) properties.get(LAST_X);
            Double lastY = (Double) properties.get(LAST_Y);
            x = lastX != null ? lastX : window.getX();
            y = lastY != null ? lastY : window.getY();
            width = window.getWidth();
            height = window.getHeight();
        }

        public void apply(Window window) {
            if (maximized && window instanceof Stage) {
                ((Stage) window).setMaximized(true);
            } else {
                applyLocationAndSize(window);
            }
        }

        public void applyLocationAndSize(Window window) {
            window.setX(x);
            window.setY(y);
            window.setWidth(width);
            window.setHeight(height);
        }
    }
}
