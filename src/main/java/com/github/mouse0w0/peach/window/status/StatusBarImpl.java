package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.ListUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StatusBarImpl implements StatusBar, Disposable {
    private final Project project;

    private final StackPane pane;

    private final HBox left;
    private final HBox right;

    private final Map<String, WidgetBean> idToBeanMap = new HashMap<>();
    private final Map<Node, WidgetBean> nodeToBeanMap = new HashMap<>();
    private final Comparator<Node> nodeComparator = Comparator.comparingInt(node -> nodeToBeanMap.get(node).index);

    public StatusBarImpl(Project project) {
        this.project = project;

        left = new HBox();
        left.getStyleClass().add("left");
        left.setAlignment(Pos.CENTER_LEFT);
        StackPane.setAlignment(left, Pos.CENTER_LEFT);

        right = new HBox();
        left.getStyleClass().add("right");
        right.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setAlignment(right, Pos.CENTER_RIGHT);

        pane = new StackPane(left, right);
        pane.setId("status-bar");

        ActionManager actionManager = ActionManager.getInstance();
        ContextMenu statusBarPopupMenu = actionManager.createContextMenu((ActionGroup) actionManager.getAction("StatusBarPopupMenu"));
        pane.setOnContextMenuRequested(event -> {
            statusBarPopupMenu.show(pane, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        initialize();
    }

    private void initialize() {
        StatusBarWidgetManager manager = StatusBarWidgetManager.getInstance();
        for (StatusBarWidgetProvider provider : manager.getProviders()) {
            if (manager.isEnabled(provider) && provider.isAvailable(project)) {
                addWidget(provider.getId(), provider, manager);
            }
        }
    }

    public StackPane getNode() {
        return pane;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public StatusBarWidget getWidget(String id) {
        WidgetBean widgetBean = idToBeanMap.get(id);
        return widgetBean != null ? widgetBean.widget : null;
    }

    @Override
    public boolean hasWidget(String id) {
        return idToBeanMap.containsKey(id);
    }

    @Override
    public void addWidget(String id) {
        StatusBarWidgetManager manager = StatusBarWidgetManager.getInstance();
        StatusBarWidgetProvider provider = manager.getProvider(id);
        if (provider == null) {
            throw new IllegalArgumentException("Not found StatusBarWidgetProvider (id=" + id + ")");
        }
        if (manager.isEnabled(provider) && provider.isAvailable(project)) {
            addWidget(id, provider, manager);
        }
    }

    private void addWidget(String id, StatusBarWidgetProvider provider, StatusBarWidgetManager manager) {
        StatusBarWidget widget = provider.createWidget(project);
        StatusBarPosition position = provider.getPosition();
        Node node = widget.getNode();
        WidgetBean widgetBean = new WidgetBean(widget, position, node, manager.getIndex(id));
        idToBeanMap.put(id, widgetBean);
        nodeToBeanMap.put(node, widgetBean);
        ListUtils.binarySearchInsert(getChildren(position), node, nodeComparator);
    }

    @Override
    public void removeWidget(String id) {
        WidgetBean widgetBean = idToBeanMap.remove(id);
        if (widgetBean != null) {
            nodeToBeanMap.remove(widgetBean.node);
            getChildren(widgetBean.position).remove(widgetBean.node);
            Disposer.dispose(widgetBean.widget);
        }
    }

    private ObservableList<Node> getChildren(StatusBarPosition position) {
        if (position == StatusBarPosition.LEFT) return left.getChildren();
        else return right.getChildren();
    }

    @Override
    public void dispose() {
        for (WidgetBean widgetBean : idToBeanMap.values()) {
            Disposer.dispose(widgetBean.widget);
        }
    }

    private static final class WidgetBean {
        final StatusBarWidget widget;
        final StatusBarPosition position;
        final Node node;
        final int index;

        public WidgetBean(StatusBarWidget widget, StatusBarPosition position, Node node, int index) {
            this.widget = widget;
            this.position = position;
            this.node = node;
            this.index = index;
        }
    }
}
