package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull String getId() {
        return ID;
    }

    @Override
    public @NotNull Node getContent() {
        return text;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }
}
