package com.github.mouse0w0.peach.project.service;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.Validate;
import com.google.gson.JsonElement;
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

    private final Map<String, WindowStateBean> beanMap = new HashMap<>();

    private final EventHandler<WindowEvent> showing = event -> {
        Window window = (Window) event.getSource();
        String stateId = (String) window.getProperties().get(STATE_ID);
        WindowStateBean bean = beanMap.get(stateId);
        if (bean != null) bean.apply(window);
    };
    private final EventHandler<WindowEvent> hidden = event -> {
        Window window = (Window) event.getSource();
        String stateId = (String) window.getProperties().get(STATE_ID);
        beanMap.computeIfAbsent(stateId, k -> new WindowStateBean()).store(window);
    };

    private final ChangeListener<Number> xListener = (observable, oldValue, newValue) -> {
        ReadOnlyDoubleProperty property = (ReadOnlyDoubleProperty) observable;
        Stage stage = (Stage) property.getBean();
        if (!stage.isMaximized()) {
            stage.getProperties().put(LAST_X, oldValue.doubleValue());
        }
    };
    private final ChangeListener<Number> yListener = (observable, oldValue, newValue) -> {
        ReadOnlyDoubleProperty property = (ReadOnlyDoubleProperty) observable;
        Stage stage = (Stage) property.getBean();
        if (!stage.isMaximized()) {
            stage.getProperties().put(LAST_Y, oldValue.doubleValue());
        }
    };
    private final ChangeListener<Boolean> maximizedListener = (observable, oldValue, newValue) -> {
        ReadOnlyBooleanProperty property = (ReadOnlyBooleanProperty) observable;
        Stage stage = (Stage) property.getBean();
        String stateId = (String) stage.getProperties().get(STATE_ID);
        if (newValue) {
            beanMap.computeIfAbsent(stateId, k -> new WindowStateBean()).storeWithoutMaximize(stage);
        } else {
            beanMap.computeIfAbsent(stateId, k -> new WindowStateBean()).applyWithoutMaximized(stage);
        }
    };

    public static WindowStateManager getInstance(Project project) {
        return project.getService(WindowStateManager.class);
    }

    public void register(Window window, String stateId) {
        Validate.notEmpty(stateId);
        window.getProperties().put(STATE_ID, stateId);
        window.addEventFilter(WindowEvent.WINDOW_SHOWING, showing);
        window.addEventFilter(WindowEvent.WINDOW_HIDDEN, hidden);
        if (window instanceof Stage) {
            window.xProperty().addListener(xListener);
            window.yProperty().addListener(yListener);
            ((Stage) window).maximizedProperty().addListener(maximizedListener);
        }
    }

    public void unregister(Window window) {
        window.getProperties().remove(STATE_ID);
        window.removeEventFilter(WindowEvent.WINDOW_SHOWING, showing);
        window.removeEventFilter(WindowEvent.WINDOW_HIDDEN, hidden);
        if (window instanceof Stage) {
            window.xProperty().removeListener(xListener);
            window.yProperty().removeListener(yListener);
            ((Stage) window).maximizedProperty().removeListener(maximizedListener);
        }
    }

    @Nonnull
    @Override
    public String getStoreFile() {
        return "windowState.json";
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(beanMap);
    }

    @Override
    public void loadState(JsonElement jsonElement) {
        beanMap.putAll(JsonUtils.fromJson(jsonElement, TypeUtils.parameterize(Map.class, String.class, WindowStateBean.class)));
    }

    public static class WindowStateBean {
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

        public void storeWithoutMaximize(Window window) {
            ObservableMap<Object, Object> properties = window.getProperties();
            x = properties.containsKey(LAST_X) ? (double) properties.get(LAST_X) : window.getX();
            y = properties.containsKey(LAST_Y) ? (double) properties.get(LAST_Y) : window.getY();
            width = window.getWidth();
            height = window.getHeight();
        }

        public void apply(Window window) {
            if (window instanceof Stage) {
                ((Stage) window).setMaximized(maximized);
            }
            if (!maximized) {
                applyWithoutMaximized(window);
            }
        }

        public void applyWithoutMaximized(Window window) {
            window.setX(x);
            window.setY(y);
            window.setWidth(width);
            window.setHeight(height);
        }
    }
}
