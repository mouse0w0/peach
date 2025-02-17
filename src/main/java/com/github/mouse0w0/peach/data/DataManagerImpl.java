package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.util.WeakValueMap;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
public final class DataManagerImpl implements DataManager {
    public static final String DATA_PROVIDER = "data-provider";
    public static final String OWNER_NODE = "owner-node";
    public static final String MENU_POPUP = "menu-popup";

    @Override
    public DataContext getDataContext(@NotNull Object source) {
        return new DataContextImpl(this, Objects.requireNonNull(source));
    }

    @Override
    public DataProvider getDataProvider(@NotNull Object o) {
        if (o instanceof DataProvider) {
            return (DataProvider) o;
        }
        Map<Object, Object> properties = getProperties(o);
        if (properties != null) {
            return (DataProvider) properties.get(DATA_PROVIDER);
        }
        return null;
    }

    @Override
    public void registerDataProvider(@NotNull Object o, @NotNull DataProvider dataProvider) {
        Map<Object, Object> properties = getOrCreateProperties(o);
        if (properties == null) {
            throw new UnsupportedOperationException("Cannot register DataProvider at " + o.getClass());
        }
        properties.put(DATA_PROVIDER, dataProvider);
    }

    @Override
    public void unregisterDataProvider(@NotNull Object o) {
        Map<Object, Object> properties = getProperties(o);
        if (properties != null) {
            properties.remove(DATA_PROVIDER);
        }
    }

    private Object getData(String key, Object source) {
        for (Object o = source; o != null; o = getParent(o)) {
            DataProvider dataProvider = getDataProvider(o);
            if (dataProvider == null) continue;
            Object data = dataProvider.getData(key);
            if (data != null) return data;
        }
        return null;
    }

    private static Object getParent(Object o) {
        if (o instanceof Node node) {
            Parent parent = node.getParent();
            return parent != null ? parent : node.getScene();
        } else if (o instanceof Scene) {
            return ((Scene) o).getWindow();
        } else if (o instanceof Stage) {
            return ((Stage) o).getOwner();
        } else if (o instanceof PopupWindow popupWindow) {
            Node ownerNode = popupWindow.getOwnerNode();
            if (ownerNode != null) return ownerNode;
            ownerNode = (Node) popupWindow.getProperties().get(OWNER_NODE);
            if (ownerNode != null) return ownerNode;
            return popupWindow.getOwnerWindow();
        } else if (o instanceof MenuItem menuItem) {
            Menu parent = menuItem.getParentMenu();
            if (parent != null) {
                // Fix cannot get popup of Menu.
                parent.getProperties().put(MENU_POPUP, menuItem.getParentPopup());

                return parent;
            } else {
                ContextMenu popup = menuItem.getParentPopup();
                return popup != null ? popup : menuItem.getProperties().get(MENU_POPUP);
            }
        } else {
            return null;
        }
    }

    private static Map<Object, Object> getProperties(Object o) {
        if (o instanceof Node node) {
            return node.hasProperties() ? node.getProperties() : null;
        } else if (o instanceof Scene scene) {
            return scene.hasProperties() ? scene.getProperties() : null;
        } else if (o instanceof Window window) {
            return window.hasProperties() ? window.getProperties() : null;
        } else {
            return null;
        }
    }

    private static Map<Object, Object> getOrCreateProperties(Object o) {
        if (o instanceof Node) {
            return ((Node) o).getProperties();
        } else if (o instanceof Scene) {
            return ((Scene) o).getProperties();
        } else if (o instanceof Window) {
            return ((Window) o).getProperties();
        } else {
            return null;
        }
    }

    private static final class DataContextImpl implements DataContext {
        private final DataManagerImpl dataManager;
        private final Reference<Object> ref;
        private final Map<String, Object> cachedData = new WeakValueMap<>(new HashMap<>());

        public DataContextImpl(DataManagerImpl dataManager, Object source) {
            this.dataManager = dataManager;
            this.ref = new WeakReference<>(source);
        }

        @Override
        public Object getData(@NotNull String key) {
            Object data = cachedData.get(key);
            if (data != null) return data;

            Object source = ref.get();
            if (source == null) return null;
            data = dataManager.getData(key, source);
            if (data != null) cachedData.put(key, data);
            return data;
        }
    }
}
