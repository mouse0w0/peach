package com.github.mouse0w0.peach.welcome;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.icon.Icons;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.service.RecentProjectInfo;
import com.github.mouse0w0.peach.service.RecentProjectsManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.Comparator;

public class WelcomeUI extends BorderPane {

    private static final boolean showWelcomeIfNoProjectOpened = false;

    private static Stage stage;

    private ListView<RecentProjectInfo> recentProjects;

    private final ContextMenu contextMenu;
    private final EventHandler<Event> onContextMenuRequested;

    static {
        Peach.getEventBus().addListener(WelcomeUI::onOpenedProject);
        Peach.getEventBus().addListener(WelcomeUI::onClosedProject);
    }

    public static void show() {
        stage = new Stage();
        stage.setScene(new Scene(new WelcomeUI()));
        stage.setTitle(I18n.translate("welcome.title"));
        stage.getIcons().setAll(Icons.Peach_16x);
        stage.setResizable(false);
        stage.setOnHidden(event -> {
            if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
                Peach.getInstance().exit();
            }
        });
        stage.show();
    }

    private static void onOpenedProject(ProjectEvent.Opened event) {
        stage.hide();
    }

    private static void onClosedProject(ProjectEvent.Closed event) {
        if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
            if (showWelcomeIfNoProjectOpened) show();
            else Peach.getInstance().exit();
        }
    }

    public WelcomeUI() {
        FXUtils.addStyleSheet(this, "style/style.css");

        setId("welcome");
        setPrefSize(600, 400);

        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup filePopupMenu = (ActionGroup) actionManager.getAction("RecentProjectsListViewPopupMenu");
        contextMenu = actionManager.createContextMenu(filePopupMenu);
        onContextMenuRequested = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                contextMenu.getProperties().put(Node.class, event.getSource());
            }
        };

        recentProjects = new ListView<>();
        recentProjects.setId("recent-projects");
        recentProjects.setPrefWidth(250);
        recentProjects.setCellFactory(list -> new Cell());
        recentProjects.getItems().addAll(RecentProjectsManager.getInstance().getRecentProjects());
        recentProjects.getItems().sort(Comparator.comparingLong(RecentProjectInfo::getLatestOpenTimestamp).reversed());
        DataManager.getInstance().registerDataProvider(recentProjects, key -> {
            if (DataKeys.SELECTED_ITEM.is(key)) return recentProjects.getSelectionModel().getSelectedItem();
            else if (DataKeys.SELECTED_ITEMS.is(key)) return recentProjects.getSelectionModel().getSelectedItems();
            else return null;
        });
        setLeft(recentProjects);

        Button newProject = actionManager.createButton(actionManager.getAction("NewProject"));
        newProject.setText(I18n.translate("welcome.NewProject.text"));
        newProject.setGraphic(new ImageView(Icons.Action.NewProject));
        Button openProject = actionManager.createButton(actionManager.getAction("OpenProject"));
        Button donate = actionManager.createButton(actionManager.getAction("Donate"));

        VBox vBox = new VBox(10, newProject, openProject, donate);
        vBox.setAlignment(Pos.CENTER);
        StackPane.setAlignment(vBox, Pos.CENTER);

        Label version = new Label(Peach.getInstance().getVersion().toString());
        version.setId("version");
        StackPane.setAlignment(version, Pos.BOTTOM_RIGHT);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(vBox, version);

        setCenter(stackPane);
    }

    private class Cell extends ListCell<RecentProjectInfo> {

        public Cell() {
            setContextMenu(contextMenu);
            setOnContextMenuRequested(onContextMenuRequested);
            setOnMouseClicked(event -> {
                if (isEmpty()) return;
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    ProjectManager.getInstance().openProject(Paths.get(getItem().getPath()));
                }
            });
        }

        @Override
        protected void updateItem(RecentProjectInfo item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText(item.getName() + "\n" + item.getPath());
            }
        }
    }
}
