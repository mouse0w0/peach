package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class TextWidget implements StatusBarWidget {
    public static final String ID = "Text";

    public static TextWidget getInstance(Project project) {
        return (TextWidget) WindowManager.getInstance().getWindow(project).getStatusBar().getWidget(ID);
    }

    public static TextWidget getFocusedInstance() {
        return (TextWidget) WindowManager.getInstance().getFocusedWindow().getStatusBar().getWidget(ID);
    }

    private final Text text = new Text();

    private TextWidget() {
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public @NotNull Node getNode() {
        return text;
    }

    @Override
    public void dispose() {
    }

    public static final class Provider implements StatusBarWidgetProvider {

        @Override
        public String getId() {
            return ID;
        }

        @Override
        public String getDisplayName() {
            return AppL10n.localize("statusBarWidget.Text");
        }

        @Override
        public StatusBarPosition getPosition() {
            return StatusBarPosition.LEFT;
        }

        @Override
        public StatusBarWidget createWidget(Project project) {
            return new TextWidget();
        }
    }
}
