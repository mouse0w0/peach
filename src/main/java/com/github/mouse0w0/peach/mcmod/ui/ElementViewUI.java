package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.SkinUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.FileWatcher;
import com.github.mouse0w0.peach.wizard.Wizard;
import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;

public class ElementViewUI extends ScrollPane {

    private final Project project;

    private FlowPane content;

    private Entry openingMenuEntry;
    private ContextMenu entryMenu;

    private FileWatcher fileWatcher;

    public ElementViewUI(Project project) {
        this.project = project;
        setFitToWidth(true);
        content = new FlowPane();
        setContent(content);

        entryMenu = new ContextMenu();
        MenuItem open = new MenuItem(I18n.translate("common.open"));
        open.setOnAction(event -> openingMenuEntry.openWizard());
        MenuItem remove = new MenuItem(I18n.translate("common.remove"));
        remove.setOnAction(event -> openingMenuEntry.delete());
        entryMenu.getItems().addAll(open, remove);

        Path sourcesPath = project.getData(McModDataKeys.SOURCES_PATH);

        try {
            FileUtils.createDirectoriesIfNotExists(sourcesPath);
            fileWatcher = new FileWatcher(sourcesPath,
                    SensitivityWatchEventModifier.HIGH, ExtendedWatchEventModifier.FILE_TREE);
            fileWatcher.addListener(StandardWatchEventKinds.ENTRY_CREATE, path ->
                    Platform.runLater(() -> addEntry(path)));
            fileWatcher.addListener(StandardWatchEventKinds.ENTRY_DELETE, path ->
                    Platform.runLater(() -> removeEntry(path)));
            fileWatcher.start();
            Files.walk(sourcesPath).forEach(this::addEntry);
        } catch (IOException ignored) {
        }
    }

    private void addEntry(Path file) {
        if (!Files.isRegularFile(file)) return;

        ElementType<?> definition = ElementManager.getInstance().getElement(file);
        if (definition == null) return;

        content.getChildren().add(new Entry(file, definition));
    }

    private void removeEntry(Path file) {
        content.getChildren().removeIf(node -> ((Entry) node).getFile().equals(file));
    }

    private class Entry extends Control {
        private final Path file;
        private final ElementType<?> definition;

        @FXML
        private Rectangle icon;
        @FXML
        private Text title;
        @FXML
        private Text description;

        public Entry(Path file, ElementType<?> definition) {
            this.file = file;
            this.definition = definition;
            setSkin(SkinUtils.create(this, FXUtils.loadFXML(null, this, "ui/mcmod/ElementViewEntry.fxml")));

            String fileName = file.getFileName().toString();
            title.setText(fileName.substring(0, fileName.indexOf('.')));

            description.setText(I18n.translate(definition.getTranslationKey()));

            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) openWizard();
            });
            setOnContextMenuRequested(event -> openingMenuEntry = this);
            setContextMenu(entryMenu);
        }

        public Path getFile() {
            return file;
        }

        public ElementType<?> getDefinition() {
            return definition;
        }

        public void openWizard() {
            Element<?> element = definition.load(file);
            Wizard wizard = definition.createWizard(element);
            WindowManager.getInstance().getWindow(project).openTab(Wizard.createTab(wizard));
        }

        public void delete() {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
