package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionGroups;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.data.DataProvider;
import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.file.FileAppearance;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.fileWatch.FileChangeListener;
import com.github.mouse0w0.peach.fileWatch.ProjectFileWatcher;
import com.github.mouse0w0.peach.javafx.ClipboardUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ProjectView implements Disposable, DataProvider {
    public static final PseudoClass DROP_HOVER = PseudoClass.getPseudoClass("drop-hover");

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectView.class);

    private final Project project;
    private final Path projectPath;

    private final Map<Path, TreeItem<Path>> itemMap = new HashMap<>();
    private final Map<Path, TreeItem<Path>> expandedItemMap = new HashMap<>();

    private final InvalidationListener expandedListener = new InvalidationListener() {
        @Override
        @SuppressWarnings("unchecked")
        public void invalidated(Observable observable) {
            BooleanProperty expanded = (BooleanProperty) observable;
            TreeItem<Path> item = (TreeItem<Path>) expanded.getBean();
            if (expanded.get()) {
                expand(item);
            }
        }
    };

    private Comparator<TreeItem<Path>> comparator = Comparator.comparing(TreeItem::getValue, (o1, o2) -> {
        boolean isDir1 = Files.isDirectory(o1);
        boolean isDir2 = Files.isDirectory(o2);
        if (isDir1 == isDir2) return o1.compareTo(o2);
        else return isDir1 ? -1 : 1;
    });

    private TreeView<Path> treeView;

    private ContextMenu contextMenu;
    private EventHandler<Event> onContextMenuRequested;

    private FileChangeListener fileChangeListener;

    public static ProjectView getInstance(Project project) {
        return project.getService(ProjectView.class);
    }

    public ProjectView(Project project) {
        this.project = project;
        this.projectPath = project.getPath();
    }

    public Comparator<TreeItem<Path>> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<TreeItem<Path>> comparator) {
        this.comparator = comparator;
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
        onContextMenuRequested = event -> contextMenu.getProperties().put(Node.class, event.getSource());

        TreeItem<Path> root = createTreeItem(projectPath);
        root.setExpanded(true);
        treeView.setRoot(root);

        fileChangeListener = new FileChangeListener() {
            @Override
            public void onFileCreate(ProjectFileWatcher watcher, Path path) {
                ProjectView.this.onFileCreate(path);
            }

            @Override
            public void onFileDelete(ProjectFileWatcher watcher, Path path) {
                ProjectView.this.onFileDelete(path);
            }
        };
        ProjectFileWatcher.getInstance(project).addListener(fileChangeListener);
        return treeView;
    }

    private TreeItem<Path> createTreeItem(Path path) {
        TreeItem<Path> treeItem = new TreeItem<>(path);

        if (Files.isDirectory(path)) {
            treeItem.expandedProperty().addListener(expandedListener);
            if (FileUtils.notEmptyDirectory(path)) {
                treeItem.getChildren().add(new TreeItem<>());
            }
        }

        itemMap.put(path, treeItem);
        return treeItem;
    }

    private void expand(TreeItem<Path> parentItem) {
        Path parent = parentItem.getValue();
        if (expandedItemMap.containsKey(parent)) return;

        ObservableList<TreeItem<Path>> children = parentItem.getChildren();
        children.clear();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(parent)) {
            for (Path child : stream) {
                children.add(createTreeItem(child));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        children.sort(comparator);
        expandedItemMap.put(parent, parentItem);
    }

    private void onFileCreate(Path path) {
        Platform.runLater(() -> {
            Path parentPath = path.getParent();
            TreeItem<Path> parent = expandedItemMap.get(parentPath);
            if (parent == null) { // Parent not expanded.
                parent = itemMap.get(parentPath);
                if (parent == null) return;

                // Parent can be expanded now.
                if (parent.getChildren().size() == 0) {
                    parent.getChildren().add(new TreeItem<>());
                }
                return;
            }

            ObservableList<TreeItem<Path>> children = parent.getChildren();
            children.add(createTreeItem(path));
            children.sort(comparator);
        });
    }

    private void onFileDelete(Path path) {
        Platform.runLater(() -> {
            TreeItem<Path> treeItem = itemMap.get(path);
            if (treeItem == null) return;
            TreeItem<Path> parent = treeItem.getParent();
            parent.getChildren().remove(treeItem);
        });
    }

    @Override
    public void dispose() {
        ProjectFileWatcher.getInstance(project).removeListener(fileChangeListener);
    }

    @Override
    public Object getData(@Nonnull String key) {
        if (DataKeys.PATH.is(key) || DataKeys.SELECTED_ITEM.is(key)) {
            TreeItem<Path> selectedItem = treeView.getSelectionModel().getSelectedItem();
            return selectedItem != null ? selectedItem.getValue() : null;
        } else if (DataKeys.SELECTED_ITEMS.is(key)) {
            List<TreeItem<Path>> selectedItems = treeView.getSelectionModel().getSelectedItems();
            List<Path> result = new ArrayList<>(selectedItems.size());
            for (TreeItem<Path> selectedItem : selectedItems) {
                result.add(selectedItem.getValue());
            }
            return result;
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

    private class Cell extends TreeCell<Path> implements DataProvider, FileCell {
        private ImageView imageView;

        public Cell() {
            disableProperty().bind(emptyProperty());

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
        public Object getData(@Nonnull String key) {
            return DataKeys.PATH.is(key) ? getItem() : null;
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
