package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class StatusBarInfo implements StatusBarWidget {
    public static final String ID = "StatusBarInfo";

    public static StatusBarInfo getInstance(Project project) {
        StatusBar statusBar = WindowManager.getInstance().getWindow(project).getStatusBar();
        StatusBarWidget widget = statusBar.getWidget(ID);
        if (widget == null) {
            widget = new StatusBarInfo();
            statusBar.addWidget(widget, StatusBar.Position.LEFT);
        }
        return (StatusBarInfo) widget;
    }

    public static StatusBarInfo getFocusedInstance() {
        StatusBar statusBar = WindowManager.getInstance().getFocusedWindow().getStatusBar();
        StatusBarWidget widget = statusBar.getWidget(ID);
        if (widget == null) {
            widget = new StatusBarInfo();
            statusBar.addWidget(widget, StatusBar.Position.LEFT);
        }
        return (StatusBarInfo) widget;
    }

    private final Text text = new Text();

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public Node getContent() {
        return text;
    }

    @Override
    public void install(StatusBar statusBar) {
    }
}
