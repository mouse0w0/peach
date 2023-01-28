package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.Validate;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class StatusBarImpl implements StatusBar {

    private final Project project;

    private final StackPane content;

    private final HBox left;
    private final HBox center;
    private final HBox right;

    private final Map<String, WidgetBean> widgetMap = new HashMap<>();

    public StatusBarImpl(Project project) {
        this.project = project;

        content = new StackPane();
        content.setId("status-bar");

        left = new HBox();
        left.setAlignment(Pos.CENTER_LEFT);
        StackPane.setAlignment(left, Pos.CENTER_LEFT);

        center = new HBox();
        center.setAlignment(Pos.CENTER);
        StackPane.setAlignment(center, Pos.CENTER);

        right = new HBox();
        right.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setAlignment(right, Pos.CENTER_RIGHT);

        content.getChildren().addAll(left, center, right);
    }

    public StackPane getContent() {
        return content;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public StatusBarWidget getWidget(String id) {
        WidgetBean widgetBean = widgetMap.get(id);
        return widgetBean != null ? widgetBean.getWidget() : null;
    }

    @Override
    public boolean hasWidget(String id) {
        return widgetMap.containsKey(id);
    }

    @Override
    public void addWidget(@Nonnull StatusBarWidget widget) {
        addWidget(widget, Position.RIGHT, null, null);
    }

    @Override
    public void addWidget(@Nonnull StatusBarWidget widget, @Nonnull Position position) {
        addWidget(widget, position, null, null);
    }

    @Override
    public void addWidget(@Nonnull StatusBarWidget widget, @Nonnull Position position, Anchor anchor) {
        addWidget(widget, position, anchor, null);
    }

    @Override
    public void addWidget(@Nonnull StatusBarWidget widget, Anchor anchor, String anchorId) {
        addWidget(widget, Position.RIGHT, anchor, anchorId);
    }

    @Override
    public void addWidget(@Nonnull StatusBarWidget widget, Position position, Anchor anchor, String anchorId) {
        String id = Validate.notEmpty(widget.getId(), "id");
        Node content = Validate.notNull(widget.getContent(), "content");
        Validate.notNull(position, "position");

        ObservableList<Node> children = getChildren(position);
        WidgetBean anchorWidget = widgetMap.get(anchorId);

        widgetMap.put(id, new WidgetBean(widget, content, position, anchor, anchorId));

        if (anchorWidget != null && anchor != null) {
            int anchorIndex = children.indexOf(anchorWidget.getContent());
            if (anchorIndex == -1) {
                children.add(content);
            } else {
                children.add(anchor == Anchor.BEFORE ? anchorIndex : anchorIndex + 1, content);
            }
        } else {
            children.add(anchor == Anchor.BEFORE ? 0 : children.size(), content);
        }

        widget.install(this);
    }

    @Override
    public boolean removeWidget(String id) {
        WidgetBean widgetBean = widgetMap.remove(id);
        if (widgetBean == null) return false;

        getChildren(widgetBean.getPosition()).remove(widgetBean.getContent());
        return true;
    }

    private ObservableList<Node> getChildren(Position position) {
        if (position == Position.LEFT) return left.getChildren();
        else if (position == Position.CENTER) return center.getChildren();
        else return right.getChildren();
    }

    public static class WidgetBean {
        private final StatusBarWidget widget;
        private final Node content;
        private final StatusBar.Position position;
        private final StatusBar.Anchor anchor;
        private final String anchorId;

        public WidgetBean(StatusBarWidget widget, Node content, Position position, Anchor anchor, String anchorId) {
            this.widget = widget;
            this.content = content;
            this.position = position;
            this.anchor = anchor;
            this.anchorId = anchorId;
        }

        public StatusBarWidget getWidget() {
            return widget;
        }

        public Node getContent() {
            return content;
        }

        public Position getPosition() {
            return position;
        }

        public Anchor getAnchor() {
            return anchor;
        }

        public String getAnchorId() {
            return anchorId;
        }
    }
}
