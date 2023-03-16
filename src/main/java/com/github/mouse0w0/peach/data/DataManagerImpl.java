package com.github.mouse0w0.peach.data;

import com.google.common.collect.MapMaker;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.annotation.Nonnull;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;

public final class DataManagerImpl implements DataManager {
    @Override
    public DataContext getDataContext(@Nonnull Object source) {
        return new DataContextImpl(this, source);
    }

    @Override
    public DataProvider getDataProvider(@Nonnull Object o) {
        if (o instanceof DataProvider) return (DataProvider) o;
        Map<Object, Object> properties = getProperties(o);
        return properties != null ? (DataProvider) properties.get(DataProvider.class) : null;
    }

    @Override
    public void registerDataProvider(@Nonnull Object o, @Nonnull DataProvider dataProvider) {
        Map<Object, Object> properties = getProperties(o);
        if (properties == null) throw new IllegalArgumentException("Not found properties for " + o.getClass());
        properties.put(DataProvider.class, dataProvider);
    }

    @Override
    public void unregisterDataProvider(@Nonnull Object o) {
        Map<Object, Object> properties = getProperties(o);
        if (properties != null) properties.remove(DataProvider.class);
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

    private Object getParent(Object o) {
        if (o instanceof Node node) {
            Parent parent = node.getParent();
            return parent != null ? parent : node.getScene();
        } else if (o instanceof Scene) {
            return ((Scene) o).getWindow();
        } else if (o instanceof Stage) {
            return ((Stage) o).getOwner();
        } else if (o instanceof PopupWindow popupWindow) {
            Node ownerNode = popupWindow.getOwnerNode();
            if (ownerNode == null) {
                ownerNode = (Node) popupWindow.getProperties().get(Node.class);
            }
            return ownerNode != null ? ownerNode : popupWindow.getOwnerWindow();
        } else if (o instanceof MenuItem menuItem) {
            ContextMenu parentPopup = menuItem.getParentPopup();
            Menu parent = menuItem.getParentMenu();
            if (parent != null) {
                // Fix cannot get parent popup of Menu.
                if (parent.getParentPopup() == null) {
                    parent.getProperties().put(ContextMenu.class, parentPopup);
                }
                return parent;
            } else {
                return parentPopup != null ? parentPopup : menuItem.getProperties().get(ContextMenu.class);
            }
        } else if (o instanceof Tab) {
            return ((Tab) o).getTabPane();
        } else {
            return null;
        }
    }

    private Map<Object, Object> getProperties(Object o) {
        if (o instanceof Node) {
            return ((Node) o).getProperties();
        } else if (o instanceof Scene) {
            return ((Scene) o).getProperties();
        } else if (o instanceof Window) {
            return ((Window) o).getProperties();
        } else if (o instanceof MenuItem) {
            return ((MenuItem) o).getProperties();
        } else if (o instanceof Tab) {
            return ((Tab) o).getProperties();
        } else {
            return null;
        }
    }

    private static final class DataContextImpl implements DataContext {
        private final DataManagerImpl dataManager;
        private final Reference<Object> ref;
        private final Map<String, Object> cachedData = new MapMaker().weakValues().makeMap();

        public DataContextImpl(DataManagerImpl dataManager, Object source) {
            this.dataManager = dataManager;
            this.ref = new WeakReference<>(source);
        }

        @Override
        public Object getData(@Nonnull String key) {
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
