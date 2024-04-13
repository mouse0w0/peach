package com.github.mouse0w0.peach.projectView;

import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.data.DataProvider;
import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.file.FileAppearance;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.fileWatch.FileChangeListener;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.ClipboardUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.ListUtils;
import com.github.mouse0w0.peach.view.ViewFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ProjectView implements DataProvider, Disposable.Default {
    public static final PseudoClass DROP_HOVER = PseudoClass.getPseudoClass("drop-hover");

    private final Project project;

    private final Map<Path, TreeItem<Path>> itemMap = new HashMap<>();
    private final Map<Path, TreeItem<Path>> expandedItemMap = new HashMap<>();

    private final EventHandler<TreeItem.TreeModificationEvent<Path>> expandedEventHandler = new EventHandler<>() {
        @Override
        public void handle(TreeItem.TreeModificationEvent<Path> event) {
            TreeItem<Path> treeItem = event.getTreeItem();
            treeItem.removeEventHandler(TreeItem.branchExpandedEvent(), this);
            expand(treeItem);
        }
    };

    private final Comparator<TreeItem<Path>> comparator = Comparator.comparing(TreeItem::getValue, (o1, o2) -> {
        boolean isDir1 = Files.isDirectory(o1);
        boolean isDir2 = Files.isDirectory(o2);
        if (isDir1 == isDir2) return o1.compareTo(o2);
        else return isDir1 ? -1 : 1;
    });

    private final TreeView<Path> treeView;

    private final ContextMenu contextMenu;

    public static ProjectView getInstance(Project project) {
        return project.getService(ProjectView.class);
    }

    public ProjectView(Project project) {
        this.project = project;

        treeView = new TreeView<>();
        treeView.setId("project-view");
        treeView.setCellFactory(t -> new Cell());
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        DataManager.getInstance().registerDataProvider(treeView, this);

        ActionManager actionManager = ActionManager.getInstance();
        contextMenu = actionManager.createContextMenu((ActionGroup) actionManager.getAction("ProjectViewPopupMenu"));

        TreeItem<Path> root = createTreeItem(project.getPath());
        root.setExpanded(true);
        treeView.setRoot(root);

        project.getMessageBus().connect(this).subscribe(FileChangeListener.TOPIC, new FileChangeListener() {
            @Override
            public void onFileCreate(Path path) {
                ProjectView.this.onFileCreate(path);
            }

            @Override
            public void onFileDelete(Path path) {
                ProjectView.this.onFileDelete(path);
            }
        });
    }

    private Node getNode() {
        return treeView;
    }

    private TreeItem<Path> createTreeItem(Path path) {
        TreeItem<Path> treeItem = new TreeItem<>(path);

        if (Files.isDirectory(path) && FileUtils.notEmptyDirectory(path)) {
            treeItem.addEventHandler(TreeItem.branchExpandedEvent(), expandedEventHandler);
            treeItem.getChildren().add(new TreeItem<>());
        }

        itemMap.put(path, treeItem);
        return treeItem;
    }

    private void expand(TreeItem<Path> treeItem) {
        Path path = treeItem.getValue();
        if (expandedItemMap.containsKey(path)) return;

        ObservableList<TreeItem<Path>> children = treeItem.getChildren();
        children.clear();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path child : stream) {
                children.add(createTreeItem(child));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        children.sort(comparator);
        expandedItemMap.put(path, treeItem);
    }

    private void onFileCreate(Path path) {
        Platform.runLater(() -> {
            Path parentPath = path.getParent();
            TreeItem<Path> parent = expandedItemMap.get(parentPath);
            if (parent == null) { // Parent is not expanded.
                parent = itemMap.get(parentPath);
                if (parent != null && parent.getChildren().isEmpty()) {
                    // Parent is expandable now.
                    parent.addEventHandler(TreeItem.branchExpandedEvent(), expandedEventHandler);
                    parent.getChildren().add(new TreeItem<>());
                }
            } else {
                ListUtils.binarySearchInsert(parent.getChildren(), createTreeItem(path), comparator);
            }
        });
    }

    private void onFileDelete(Path path) {
        Platform.runLater(() -> {
            TreeItem<Path> treeItem = itemMap.remove(path);
            if (treeItem != null) {
                TreeItem<Path> parent = treeItem.getParent();
                if (parent != null) {
                    parent.getChildren().remove(treeItem);
                }

                expandedItemMap.remove(path);
            }
        });
    }

    @Override
    public Object getData(@NotNull String key) {
        if (DataKeys.PATH.is(key)) {
            TreeItem<Path> selectedItem = treeView.getSelectionModel().getSelectedItem();
            return selectedItem != null ? selectedItem.getValue() : null;
        } else if (DataKeys.PATHS.is(key)) {
            return ListUtils.map(treeView.getSelectionModel().getSelectedItems(), TreeItem::getValue);
        } else {
            return null;
        }
    }

    public static class Factory implements ViewFactory {
        @Override
        public Node createViewContent(Project project) {
            return ProjectView.getInstance(project).getNode();
        }
    }

    private class Cell extends TreeCell<Path> implements DataProvider, FileCell {
        private Icon icon;
        private ImageView imageView;

        public Cell() {
            disableProperty().bind(emptyProperty());

            setContextMenu(contextMenu);
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
                List<TreeItem<Path>> selectedItems = selectionModel.getSelectedItems();
                List<File> files = new ArrayList<>(selectedItems.size());
                for (TreeItem<Path> selectedItem : selectedItems) {
                    files.add(selectedItem.getValue().toFile());
                }
                content.putFiles(files);
                dragboard.setContent(content);
            });
            setOnDragOver(event -> {
                event.consume();
                if (event.getGestureSource() == event.getTarget()) return;

                Path path = getItem();
                if (path == null || Files.isRegularFile(path)) return;

                Dragboard dragboard = event.getDragboard();
                if (!dragboard.hasFiles()) return;

                File file = path.toFile();
                if (dragboard.getFiles().contains(file)) return;

                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            });
            setOnDragDropped(event -> {
                event.consume();
                Dragboard dragboard = event.getDragboard();
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = ClipboardUtils.copyOf(systemClipboard);
                systemClipboard.setContent(Collections.singletonMap(DataFormat.FILES, dragboard.getFiles()));
                ActionManager.getInstance().perform("Paste", event.getSource());
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
        }

        @Override
        protected void updateItem(Path item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                FileAppearance.process(item, this);
            }
        }

        @Override
        public Object getData(@NotNull String key) {
            return DataKeys.PATH.is(key) ? getItem() : null;
        }

        @Override
        public Icon getIcon() {
            return icon;
        }

        @Override
        public void setIcon(Icon icon) {
            this.icon = icon;

            if (imageView == null) {
                imageView = new ImageView();
            }

            imageView.setImage(icon.getImage());

            if (getGraphic() == null) {
                setGraphic(imageView);
            }
        }
    }
}
