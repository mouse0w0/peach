package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.Peach;
import com.google.common.collect.MapMaker;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.annotation.Nonnull;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;

public class DataManager {

    public static DataManager getInstance() {
        return Peach.getInstance().getService(DataManager.class);
    }

    public DataContext getDataContext(@Nonnull Object source) {
        return new DataContextImpl(source);
    }

    public void registerDataProvider(@Nonnull Object o, @Nonnull DataProvider dataProvider) {
        Map<Object, Object> properties = getProperties(o);
        if (properties == null) throw new IllegalArgumentException("Not found properties for " + o.getClass());
        properties.put(DataProvider.class, dataProvider);
    }

    public void unregisterDataProvider(@Nonnull Object o) {
        Map<Object, Object> properties = getProperties(o);
        if (properties != null) properties.remove(DataProvider.class);
    }

    public DataProvider getDataProvider(@Nonnull Object o) {
        Map<Object, Object> properties = getProperties(o);
        return properties != null ? (DataProvider) properties.get(DataProvider.class) : null;
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
        if (o instanceof Node) {
            Node node = (Node) o;
            Parent parent = node.getParent();
            return parent != null ? parent : node.getScene();
        } else if (o instanceof Scene) {
            return ((Scene) o).getWindow();
        } else if (o instanceof Stage) {
            return ((Stage) o).getOwner();
        } else if (o instanceof PopupWindow) {
            PopupWindow popupWindow = (PopupWindow) o;
            Node ownerNode = popupWindow.getOwnerNode();
            if (ownerNode == null) {
                ownerNode = (Node) popupWindow.getProperties().get(Node.class);
            }
            return ownerNode != null ? ownerNode : popupWindow.getOwnerWindow();
        } else if (o instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) o;
            Menu parent = menuItem.getParentMenu();
            return parent != null ? parent : menuItem.getParentPopup();
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
            return ((Stage) o).getProperties();
        } else if (o instanceof MenuItem) {
            return ((MenuItem) o).getProperties();
        } else {
            return null;
        }
    }

    private static class DataContextImpl implements DataContext {

        private final Reference<Object> ref;
        private final Map<String, Object> cachedData = new MapMaker().weakValues().makeMap();

        public DataContextImpl(Object source) {
            this.ref = new WeakReference<>(source);
        }

        @Override
        public Object getData(@Nonnull String key) {
            Object data = cachedData.get(key);
            if (data != null) return data;

            Object source = ref.get();
            if (source == null) return null;
            data = DataManager.getInstance().getData(key, source);
            if (data != null) cachedData.put(key, data);
            return data;
        }
    }
}
