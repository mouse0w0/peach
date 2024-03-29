package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.file.FileAppearance;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.scene.control.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileEditorManagerImpl implements FileEditorManager, Disposable.Default {
    private static final Logger LOGGER = LoggerFactory.getLogger("FileEditor");

    private final Project project;
    private final Map<Path, FileEditorTab> fileEditorTabs = new HashMap<>();

    public FileEditorManagerImpl(Project project) {
        this.project = project;
    }

    @Override
    public FileEditor getFileEditor(Path file) {
        return fileEditorTabs.get(file).getFileEditor();
    }

    @Override
    public boolean isFileOpened(Path file) {
        return fileEditorTabs.containsKey(file);
    }

    @Override
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
        FileEditorTab fileEditorTab = fileEditorTabs.get(file);
        if (fileEditorTab != null) {
            WindowManager.getInstance().getWindow(project).selectTab(fileEditorTab);
        }
    }

    private void doOpen(Path file, FileEditorProvider provider) {
        FileEditor fileEditor = provider.create(project, file);
        if (fileEditor == null) {
            return; // 由FileEditorProvider接管文件编辑，可能是打开了外部编辑器。
        }

        Disposer.register(this, fileEditor);

        FileEditorTab fileEditorTab = new FileEditorTab(fileEditor);
        FileAppearance.process(file, fileEditorTab);
        fileEditorTabs.put(file, fileEditorTab);
        WindowManager.getInstance().getWindow(project).openTab(fileEditorTab);
    }

    private void doOpenSystemEditor(Path file) {
        try {
            Desktop.getDesktop().open(file.toFile());
        } catch (IOException e) {
            LOGGER.warn("Failed to open the system editor of `{}`.", file, e);
        }
    }

    @Override
    public boolean close(FileEditor fileEditor) {
        return close(fileEditor.getFile());
    }

    @Override
    public boolean close(Path file) {
        FileEditorTab fileEditorTab = fileEditorTabs.remove(file);
        if (fileEditorTab == null) return false;

        WindowManager.getInstance().getWindow(project).removeTab(fileEditorTab);
        Disposer.dispose(fileEditorTab.getFileEditor());
        return true;
    }

    private final class FileEditorTab extends Tab implements FileCell, FileEditorHolder {
        private final FileEditor fileEditor;

        private Icon icon;

        public FileEditorTab(FileEditor fileEditor) {
            super(null, fileEditor.getNode());
            this.fileEditor = fileEditor;

            setClosable(true);
            setOnCloseRequest(event -> {
                event.consume();
                close(fileEditor.getFile());
            });
            selectedProperty().addListener(observable -> {
                if (isSelected()) {
                    fileEditor.onSelected();
                } else {
                    fileEditor.onDeselected();
                }
            });
        }

        public FileEditor getFileEditor() {
            return fileEditor;
        }

        @Override
        public Icon getIcon() {
            return icon;
        }

        @Override
        public void setIcon(Icon icon) {
            this.icon = icon;
            Icon.apply(graphicProperty(), icon);
        }
    }
}
