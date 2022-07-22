package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.file.FileAppearances;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.window.WindowManager;
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

    private final Map<Path, OpenedEditor> openedEditors = new HashMap<>();

    public static FileEditorManager getInstance(Project project) {
        return project.getService(FileEditorManager.class);
    }

    public FileEditorManager(Project project) {
        this.project = project;
    }

    public FileEditor getFileEditor(Path file) {
        return openedEditors.get(file).getFileEditor();
    }

    public boolean isFileOpened(Path file) {
        return openedEditors.containsKey(file);
    }

    public void open(Path file) {
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("The file is not regular file, file: " + file);
        }

        if (isFileOpened(file)) {
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
        OpenedEditor openedEditor = openedEditors.get(file);
        if (openedEditor != null) {
            WindowManager.getInstance().getWindow(project).selectTab(openedEditor.getTab());
        }
    }

    private void doOpen(Path file, FileEditorProvider provider) {
        FileEditor fileEditor = provider.create(project, file);
        if (fileEditor == null) {
            return; // 由FileEditorProvider接管文件编辑，可能是打开了外部编辑器。
        }

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

        openedEditors.put(file, new OpenedEditor(fileEditor, tab));
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
        OpenedEditor openedEditor = openedEditors.remove(file);
        if (openedEditor == null) return false;

        WindowManager.getInstance().getWindow(project).removeTab(openedEditor.getTab());
        Disposer.dispose(openedEditor.getFileEditor());
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

    private static class OpenedEditor {
        private final FileEditor fileEditor;
        private final Tab tab;

        public OpenedEditor(FileEditor fileEditor, Tab tab) {
            this.fileEditor = fileEditor;
            this.tab = tab;
        }

        public FileEditor getFileEditor() {
            return fileEditor;
        }

        public Tab getTab() {
            return tab;
        }
    }
}
