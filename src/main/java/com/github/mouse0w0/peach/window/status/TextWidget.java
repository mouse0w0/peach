package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionHolder;
import com.github.mouse0w0.peach.application.AppLifecycleListener;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.ProjectWindow;
import com.github.mouse0w0.peach.window.WindowManager;
import com.sun.javafx.scene.control.ContextMenuContent;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

public class TextWidget implements StatusBarWidget {
    public static final String ID = "Text";

    public static TextWidget getInstance(Project project) {
        return (TextWidget) WindowManager.getInstance().getWindow(project).getStatusBar().getWidget(ID);
    }

    public static TextWidget getFocusedInstance() {
        ProjectWindow projectWindow = WindowManager.getInstance().getFocusedWindow();
        return projectWindow == null ? null : (TextWidget) projectWindow.getStatusBar().getWidget(ID);
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

    public static final class Listener implements AppLifecycleListener {
        @Override
        public void appStarted() {
            Window.getWindows().addListener((ListChangeListener<Window>) c -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (Window window : c.getAddedSubList()) {
                            if (window instanceof ContextMenu contextMenu) {
                                setup(contextMenu);
                            }
                        }
                    }
                }
            });
        }

        private static final InvalidationListener HOVER_LISTENER = observable -> {
            TextWidget textWidget = TextWidget.getFocusedInstance();
            if (textWidget == null) return;

            ReadOnlyBooleanProperty hoverProperty = (ReadOnlyBooleanProperty) observable;
            if (hoverProperty.get()) {
                Node node = (Node) hoverProperty.getBean();
                MenuItem menuItem = ((ContextMenuContent.MenuItemContainer) node).getItem();
                Action action = ((ActionHolder) menuItem).getAction();
                textWidget.setText(action.getDescription());
            } else {
                textWidget.setText(null);
            }
        };

        private static void setup(ContextMenu contextMenu) {
            Parent root = contextMenu.getScene().getRoot();
            for (Node node : root.lookupAll(".menu-item")) {
                MenuItem menuItem = ((ContextMenuContent.MenuItemContainer) node).getItem();
                if (menuItem instanceof ActionHolder) {
                    node.hoverProperty().addListener(HOVER_LISTENER);
                }
            }
        }
    }
}
