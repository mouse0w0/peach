package com.github.mouse0w0.peach.project.service;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.service.PersistentService;
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

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class WindowStateManager implements PersistentService {
    private static final String STATE_ID = "StateId";
    private static final String LAST_X = "LastX";
    private static final String LAST_Y = "LastY";

    private final Map<String, WindowState> stateMap = new HashMap<>();

    private final EventHandler<WindowEvent> hidden = event -> {
        Window window = (Window) event.getSource();
        String stateId = (String) window.getProperties().get(STATE_ID);
        stateMap.computeIfAbsent(stateId, k -> new WindowState()).store(window);
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
        String stateId = (String) window.getProperties().get(STATE_ID);
        if (maximizedProperty.get()) {
            stateMap.computeIfAbsent(stateId, k -> new WindowState()).storeWhenMaximized(window);
        } else {
            WindowState state = stateMap.get(stateId);
            if (state != null) state.applyWhenNotMaximized(window);
        }
    };

    public static WindowStateManager getInstance(Project project) {
        return project.getService(WindowStateManager.class);
    }

    public void register(@Nonnull Window window, @Nonnull String stateId) {
        Validate.notNull(window);
        Validate.notEmpty(stateId);

        WindowState state = stateMap.get(stateId);
        if (state != null) state.apply(window);

        window.getProperties().put(STATE_ID, stateId);
        window.addEventFilter(WindowEvent.WINDOW_HIDDEN, hidden);
        if (window instanceof Stage) {
            window.xProperty().addListener(xListener);
            window.yProperty().addListener(yListener);
            ((Stage) window).maximizedProperty().addListener(maximizedListener);
        }
    }

    @Nonnull
    @Override
    public String getStoreFile() {
        return "windowState.json";
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(stateMap);
    }

    @Override
    public void loadState(JsonElement jsonElement) {
        stateMap.putAll(JsonUtils.fromJson(jsonElement, TypeUtils.parameterize(Map.class, String.class, WindowState.class)));
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
            if (maximized) {
                if (window instanceof Stage) {
                    ((Stage) window).setMaximized(true);
                }
            } else {
                applyWhenNotMaximized(window);
            }
        }

        public void applyWhenNotMaximized(Window window) {
            window.setX(x);
            window.setY(y);
            window.setWidth(width);
            window.setHeight(height);
        }
    }
}
