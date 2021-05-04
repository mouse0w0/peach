package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.file.FileAppearances;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.wm.WindowManager;
import javafx.scene.Node;
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
            return;
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

        FileTab tab = new FileTab(null, fileEditor.getNode());
        tab.getProperties().put(FileEditor.class, fileEditor);
        FileAppearances.apply(file, tab);

        String name = fileEditor.getName();
        if (name != null) tab.setText(FileUtils.getFileName(file));
        Image icon = fileEditor.getIcon();
        if (icon != null) tab.setIcon(icon);

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

    private static class FileTab extends Tab implements FileCell {
        private ImageView imageView;

        public FileTab(String text, Node content) {
            super(text, content);
        }

        @Override
        public Image getIcon() {
            return imageView == null ? null :
                    (getGraphic() != imageView ? null : imageView.getImage());
        }

        @Override
        public void setIcon(Image icon) {
            if (imageView == null) {
                imageView = new ImageView(icon);
            }

            imageView.setImage(icon);

            if (getGraphic() != imageView) {
                setGraphic(imageView);
            }
        }
    }
}
