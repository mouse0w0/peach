package com.github.mouse0w0.peach.mcmod.view;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.Disposable;
import com.github.mouse0w0.peach.util.NioFileWatcher;
import com.github.mouse0w0.peach.view.ViewFactory;
import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExplorerView implements Disposable {
    private final Project project;
    private final Path rootPath;

    private final Map<Path, TreeItem<Path>> itemMap = new HashMap<>();

    private TreeView<Path> treeView;

    private NioFileWatcher fileWatcher;

    public static ExplorerView getInstance(Project project) {
        return project.getService(ExplorerView.class);
    }

    public ExplorerView(Project project) {
        this.project = project;
        this.rootPath = project.getPath();
    }

    public Node initViewContent() {
        treeView = new TreeView<>();
        treeView.setId("explorer-view");
        treeView.setCellFactory(t -> new Cell());
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TreeItem<Path> root = createTreeItem(rootPath);
        root.setExpanded(true);
        treeView.setRoot(root);

        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    addPath(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (!rootPath.equals(dir)) {
                        addPath(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }

        fileWatcher = new NioFileWatcher(rootPath, SensitivityWatchEventModifier.LOW, ExtendedWatchEventModifier.FILE_TREE);
        fileWatcher.addListener(StandardWatchEventKinds.ENTRY_CREATE, this::addPath);
        fileWatcher.addListener(StandardWatchEventKinds.ENTRY_DELETE, path -> {
            TreeItem<Path> treeItem = itemMap.get(path);
            TreeItem<Path> parent = treeItem.getParent();
            if (parent == null) throw new IllegalStateException("Cannot delete root");
            parent.getChildren().remove(treeItem);
        });
        fileWatcher.start();
        return treeView;
    }

    private void addPath(Path path) {
        Path parentPath = path.getParent();
        TreeItem<Path> parent = itemMap.get(parentPath);
        if (parent == null) throw new IllegalStateException("Parent not found");
        parent.getChildren().add(createTreeItem(path));
    }

    private TreeItem<Path> createTreeItem(Path path) {
        TreeItem<Path> treeItem = new TreeItem<>(path);
        itemMap.put(path, treeItem);
        return treeItem;
    }

    @Override
    public void dispose() {
        fileWatcher.stop();
    }

    public static class Factory implements ViewFactory {
        @Override
        public Node createViewContent(Project project) {
            return ExplorerView.getInstance(project).initViewContent();
        }
    }

    private static class Cell extends TreeCell<Path> {
        public Cell() {
            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && Files.isRegularFile(getItem())) {
                    try {
                        Desktop.getDesktop().open(getItem().toFile());
                    } catch (IOException ignored) {
                    }
                }

            });
            setOnDragDetected(event -> {
                MultipleSelectionModel<TreeItem<Path>> selectionModel = getTreeView().getSelectionModel();
                if (selectionModel.isEmpty()) return;
                Dragboard dragboard = startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                List<File> files = selectionModel.getSelectedItems()
                        .stream()
                        .map(treeItem -> treeItem.getValue().toFile())
                        .collect(Collectors.toList());
                content.putFiles(files);
                dragboard.setContent(content);
            });
        }

        @Override
        protected void updateItem(Path item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText(item.getFileName().toString());
            }
        }
    }
}
