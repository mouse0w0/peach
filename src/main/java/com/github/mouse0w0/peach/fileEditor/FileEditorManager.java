package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.file.FileAppearances;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileEditorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileEditorManager.class);

    private final Project project;

    private final Map<Path, Tab> openedFiles = new HashMap<>();

    public static FileEditorManager getInstance(Project project) {
        return project.getService(FileEditorManager.class);
    }

    public FileEditorManager(Project project) {
        this.project = project;
    }

    public void open(Path file) {
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("The file is not regular file, file: " + file);
        }

        if (openedFiles.containsKey(file)) {
            focusFileEditor(file);
        }

        for (FileEditorProvider provider : FileEditorProvider.EXTENSION_POINT.getExtensions()) {
            if (provider.accept(file)) {
                doOpen(file, provider);
                return;
            }
        }
        doOpenSystemEditor(file);
    }

    private void focusFileEditor(Path file) {
        WindowManager.getInstance().getWindow(project).selectTab(openedFiles.get(file));
    }

    private void doOpen(Path file, FileEditorProvider provider) {
        FileEditor fileEditor = provider.create(project, file);

        ImageView imageView = new ImageView();
        Tab tab = new Tab(null, fileEditor.getContent());
        tab.getProperties().put(FileEditor.class, fileEditor);
        tab.setGraphic(imageView);

        FileAppearances.apply(file, tab.textProperty(), imageView.imageProperty());

        String name = fileEditor.getName();
        if (name != null) tab.setText(FileUtils.getFileName(file));
        Image icon = fileEditor.getIcon();
        if (icon != null) imageView.setImage(icon);

        tab.setClosable(true);
        tab.setOnCloseRequest(event -> {
            event.consume();
            close(file);
        });

        openedFiles.put(file, tab);
        WindowManager.getInstance().getWindow(project).openTab(tab);
    }

    private void doOpenSystemEditor(Path file) {
        try {
            Desktop.getDesktop().open(file.toFile());
        } catch (IOException e) {
            LOGGER.warn("Failed to open the system editor of \"" + file + "\".", e);
        }
    }

    public boolean close(FileEditor fileEditor) {
        return close(fileEditor.getFile());
    }

    public boolean close(Path file) {
        Tab tab = openedFiles.remove(file);
        if (tab == null) return false;

        WindowManager.getInstance().getWindow(project).removeTab(tab);
        FileEditor fileEditor = (FileEditor) tab.getProperties().get(FileEditor.class);
        fileEditor.dispose();
        return true;
    }
}
