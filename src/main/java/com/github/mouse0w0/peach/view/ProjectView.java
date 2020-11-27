package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionGroups;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.data.DataProvider;
import com.github.mouse0w0.peach.file.FileAppearances;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.ClipboardUtils;
import com.github.mouse0w0.peach.util.Disposable;
import com.github.mouse0w0.peach.util.NioFileWatcher;
import com.google.common.collect.ImmutableList;
import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectView implements Disposable, DataProvider {
    public static final PseudoClass DROP_HOVER = PseudoClass.getPseudoClass("drop-hover");

    private final Project project;
    private final Path rootPath;

    private final Map<Path, TreeItem<Path>> itemMap = new HashMap<>();

    private ContextMenu contextMenu;
    private EventHandler<Event> onContextMenuRequested;

    private TreeView<Path> treeView;

    private NioFileWatcher fileWatcher;

    public static ProjectView getInstance(Project project) {
        return project.getService(ProjectView.class);
    }

    public ProjectView(Project project) {
        this.project = project;
        this.rootPath = project.getPath();
    }

    public Node initViewContent() {
        treeView = new TreeView<>();
        treeView.setId("project-view");
        treeView.setCellFactory(t -> new Cell());
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        DataManager.getInstance().registerDataProvider(treeView, this);

        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup filePopupMenu = (ActionGroup) actionManager.getAction(ActionGroups.FILE_POPUP_MENU);
        contextMenu = actionManager.createContextMenu(filePopupMenu);
        onContextMenuRequested = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                contextMenu.getProperties().put(Node.class, event.getSource());
            }
        };

        TreeItem<Path> root = createTreeItem(rootPath);
        root.setExpanded(true);
        treeView.setRoot(root);

        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    addPath(file, false);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (!rootPath.equals(dir)) {
                        addPath(dir, false);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ignored) {
        }

        fileWatcher = new NioFileWatcher(rootPath, SensitivityWatchEventModifier.LOW, ExtendedWatchEventModifier.FILE_TREE);
        fileWatcher.addListener(StandardWatchEventKinds.ENTRY_CREATE, path -> addPath(path, true));
        fileWatcher.addListener(StandardWatchEventKinds.ENTRY_DELETE, path -> {
            TreeItem<Path> treeItem = itemMap.get(path);
            TreeItem<Path> parent = treeItem.getParent();
            if (parent == null) throw new IllegalStateException("Cannot delete root");
            parent.getChildren().remove(treeItem);
        });
        fileWatcher.start();
        return treeView;
    }

    private void addPath(Path path, boolean sorted) {
        Path parentPath = path.getParent();
        TreeItem<Path> parent = itemMap.get(parentPath);
        if (parent == null) throw new IllegalStateException("Parent not found");

        ObservableList<TreeItem<Path>> children = parent.getChildren();
        children.add(createTreeItem(path));
        if (sorted) {
            children.sort(Comparator.comparing(TreeItem::getValue));
        }
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

    @Override
    public Object getData(@Nonnull String key) {
        if (DataKeys.PATH.is(key) || DataKeys.SELECTED_ITEM.is(key)) {
            return treeView.getSelectionModel().getSelectedItem().getValue();
        } else if (DataKeys.SELECTED_ITEMS.is(key)) {
            return treeView.getSelectionModel().getSelectedItems()
                    .stream()
                    .map(TreeItem::getValue)
                    .collect(ImmutableList.toImmutableList());
        } else {
            return null;
        }
    }

    public static class Factory implements ViewFactory {
        @Override
        public Node createViewContent(Project project) {
            return ProjectView.getInstance(project).initViewContent();
        }
    }

    private class Cell extends TreeCell<Path> implements DataProvider {
        private final ImageView imageView = new ImageView();

        public Cell() {
            setGraphic(imageView);
            setContextMenu(contextMenu);
            setOnContextMenuRequested(onContextMenuRequested);
            setOnMouseClicked(event -> {
                Path file = getItem();
                if (event.getClickCount() == 2 && Files.isRegularFile(file)) {
                    FileEditorManager.getInstance(project).open(file);
                }
            });
            setOnDragDetected(event -> {
                MultipleSelectionModel<TreeItem<Path>> selectionModel = getTreeView().getSelectionModel();
                if (selectionModel.isEmpty()) return;
                Dragboard dragboard = startDragAndDrop(TransferMode.COPY_OR_MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(ClipboardUtils.TRANSFER_MODE, ClipboardUtils.TRANSFER_MODE_MOVE);
                List<File> files = selectionModel.getSelectedItems()
                        .stream()
                        .map(treeItem -> treeItem.getValue().toFile())
                        .collect(Collectors.toList());
                content.putFiles(files);
                dragboard.setContent(content);
            });
            setOnDragOver(event -> {
                event.consume();
                if (event.getGestureSource() == event.getTarget()) return;

                if (event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            });
            setOnDragDropped(event -> {
                event.consume();
                Dragboard dragboard = event.getDragboard();
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = ClipboardUtils.copyOf(systemClipboard);
                systemClipboard.setContent(Collections.singletonMap(DataFormat.FILES, dragboard.getFiles()));
                ActionManager.getInstance().perform("Paste", event);
                systemClipboard.setContent(content);
                event.setDropCompleted(true);
                requestFocus();
            });
            setOnDragEntered(event -> {
                if (event.getGestureSource() == event.getTarget()) return;

                if (event.getDragboard().hasFiles()) {
                    pseudoClassStateChanged(DROP_HOVER, true);
                }
            });
            setOnDragExited(event -> pseudoClassStateChanged(DROP_HOVER, false));
            DataManager.getInstance().registerDataProvider(this, this);
        }

        @Override
        protected void updateItem(Path item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                imageView.setImage(null);
            } else {
                FileAppearances.apply(item, textProperty(), imageView.imageProperty());
            }
        }

        @Override
        public Object getData(@Nonnull String key) {
            return DataKeys.PATH.is(key) ? getItem() : null;
        }
    }
}
