package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.forge.ForgeProjectDataKeys;
import com.github.mouse0w0.peach.forge.element.ElementDefinition;
import com.github.mouse0w0.peach.forge.element.ElementFile;
import com.github.mouse0w0.peach.forge.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.SkinUtils;
import com.github.mouse0w0.peach.ui.wizard.Wizard;
import com.github.mouse0w0.peach.util.FileWatcher;
import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
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

    private FileWatcher fileWatcher;

    public ElementViewUI(Project project) {
        this.project = project;
        setFitToWidth(true);
        content = new FlowPane();
        setContent(content);

        Path sourcesPath = project.getData(ForgeProjectDataKeys.SOURCES_PATH);

        fileWatcher = new FileWatcher(sourcesPath,
                SensitivityWatchEventModifier.LOW, ExtendedWatchEventModifier.FILE_TREE);
        fileWatcher.addListener(StandardWatchEventKinds.ENTRY_CREATE, path ->
                Platform.runLater(() -> addEntry(path)));
        fileWatcher.addListener(StandardWatchEventKinds.ENTRY_DELETE, path ->
                Platform.runLater(() -> removeEntry(path)));
        fileWatcher.start();

        try {
            Files.walk(sourcesPath).forEach(this::addEntry);
        } catch (IOException ignored) {
        }
    }

    private void addEntry(Path file) {
        if (!Files.isRegularFile(file)) return;

        ElementDefinition<?> definition = ElementManager.getInstance().getElement(file);
        if (definition == null) return;

        content.getChildren().add(new Entry(file, definition));
    }

    private void removeEntry(Path file) {
        content.getChildren().removeIf(node -> ((Entry) node).getFile().equals(file));
    }

    private class Entry extends Control {
        private final Path file;
        private final ElementDefinition<?> definition;

        @FXML
        private Rectangle icon;
        @FXML
        private Text title;
        @FXML
        private Text description;

        public Entry(Path file, ElementDefinition<?> definition) {
            this.file = file;
            this.definition = definition;
            setSkin(SkinUtils.create(this, FXUtils.loadFXML(null, this, "ui/forge/ElementViewEntry.fxml")));

            String fileName = file.getFileName().toString();
            title.setText(fileName.substring(0, fileName.indexOf('.')));

            description.setText(I18n.translate(definition.getTranslationKey()));

            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) openWizard();
            });
        }

        public Path getFile() {
            return file;
        }

        public ElementDefinition<?> getDefinition() {
            return definition;
        }

        public void openWizard() {
            ElementFile<?> elementFile = definition.load(file);
            Wizard wizard = definition.createWizard(elementFile);
            WindowManager.getInstance().getWindow(project).openTab(Wizard.createTab(wizard));
        }
    }
}
