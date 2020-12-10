package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.ui.control.skin.ImagePickerSkin;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class ImagePicker extends Control {

    public ImagePicker() {
        getStyleClass().setAll("image-picker");

        addEventHandler(DragEvent.DRAG_OVER, event -> {
            event.consume();
            if (event.getGestureSource() == event.getTarget()) return;

            Dragboard dragboard = event.getDragboard();
            if (!dragboard.hasFiles()) return;

            List<File> files = dragboard.getFiles();
            if (files.size() != 1) return;

            File file = files.get(0);
            if (!Utils.checkExtensions(file, extensionFilters)) return;

            event.acceptTransferModes(TransferMode.COPY);
        });
        addEventHandler(DragEvent.DRAG_DROPPED, event -> {
            event.consume();
            setFile(event.getDragboard().getFiles().get(0));
            event.setDropCompleted(true);
        });
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.MIDDLE) setFile(null);
            else showFileChooser();
        });
    }

    private ObjectProperty<File> file;

    public final ObjectProperty<File> fileProperty() {
        if (file == null) {
            file = new SimpleObjectProperty<>(this, "file");
        }
        return file;
    }

    public final File getFile() {
        return file != null ? file.get() : null;
    }

    public final void setFile(File file) {
        fileProperty().set(file);
    }

    private DoubleProperty fitWidth;

    public final DoubleProperty fitWidthProperty() {
        if (fitWidth == null) {
            fitWidth = new SimpleDoubleProperty(this, "imageWidth", 0d);
        }
        return fitWidth;
    }

    public final double getFitWidth() {
        return fitWidth != null ? fitWidth.get() : 0d;
    }

    public final void setFitWidth(double fitWidth) {
        fitWidthProperty().set(fitWidth);
    }

    private DoubleProperty fitHeight;

    public final DoubleProperty fitHeightProperty() {
        if (fitHeight == null) {
            fitHeight = new SimpleDoubleProperty(this, "fitHeight", 0d);
        }
        return fitHeight;
    }

    public final double getFitHeight() {
        return fitHeight != null ? fitHeight.get() : 0d;
    }

    public final void setFitHeight(double fitHeight) {
        fitHeightProperty().set(fitHeight);
    }

    public final void setFitSize(double fitWidth, double fitHeight) {
        setFitWidth(fitWidth);
        setFitHeight(fitHeight);
    }

    private BooleanProperty preserveRatio;

    public final BooleanProperty preserveRatioProperty() {
        if (preserveRatio == null) {
            preserveRatio = new SimpleBooleanProperty(this, "preserveRatio");
        }
        return preserveRatio;
    }

    public final boolean isPreserveRatio() {
        return preserveRatio != null && preserveRatio.get();
    }

    public final void setPreserveRatio(boolean value) {
        preserveRatioProperty().set(value);
    }

    private BooleanProperty smooth;

    public final BooleanProperty smoothProperty() {
        if (smooth == null) {
            smooth = new SimpleBooleanProperty(this, "smooth", ImageView.SMOOTH_DEFAULT);
        }
        return smooth;
    }

    public final boolean isSmooth() {
        return smooth == null ? ImageView.SMOOTH_DEFAULT : smooth.get();
    }

    public final void setSmooth(boolean value) {
        smoothProperty().set(value);
    }

    private StringProperty title;

    public final StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }

    public final String getTitle() {
        return title != null ? title.get() : null;
    }

    public final void setTitle(String title) {
        titleProperty().set(title);
    }

    private ObjectProperty<File> initialDirectory;

    public final ObjectProperty<File> initialDirectoryProperty() {
        if (initialDirectory == null) {
            initialDirectory = new SimpleObjectProperty<>(this, "initialDirectory");
        }
        return initialDirectory;
    }

    public final File getInitialDirectory() {
        return initialDirectory != null ? initialDirectory.get() : null;
    }

    public final void setInitialDirectory(File initialDirectory) {
        initialDirectoryProperty().set(initialDirectory);
    }

    private ObjectProperty<String> initialFileName;

    public final ObjectProperty<String> initialFileNameProperty() {
        if (initialFileName == null) {
            initialFileName = new SimpleObjectProperty<>(this, "initialFileName");
        }
        return initialFileName;
    }

    public final String getInitialFileName() {
        return initialFileName != null ? initialFileName.get() : null;
    }

    public final void setInitialFileName(final String value) {
        initialFileNameProperty().set(value);
    }

    private final ObservableList<FileChooser.ExtensionFilter> extensionFilters = FXCollections.observableArrayList();

    public final ObservableList<FileChooser.ExtensionFilter> getExtensionFilters() {
        return extensionFilters;
    }

    private ObjectProperty<FileChooser.ExtensionFilter> selectedExtensionFilter;

    public final ObjectProperty<FileChooser.ExtensionFilter> selectedExtensionFilterProperty() {
        if (selectedExtensionFilter == null) {
            selectedExtensionFilter = new SimpleObjectProperty<>(this, "selectedExtensionFilter");
        }
        return selectedExtensionFilter;
    }

    public final FileChooser.ExtensionFilter getSelectedExtensionFilter() {
        return selectedExtensionFilter != null ? selectedExtensionFilter.get() : null;
    }

    public final void setSelectedExtensionFilter(FileChooser.ExtensionFilter filter) {
        selectedExtensionFilterProperty().set(filter);
    }

    public void showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getTitle());
        File oldFile = getFile();
        if (oldFile == null) {
            fileChooser.setInitialDirectory(getInitialDirectory());
            fileChooser.setInitialFileName(getInitialFileName());
        } else {
            fileChooser.setInitialDirectory(oldFile.getParentFile());
            fileChooser.setInitialFileName(oldFile.getName());
        }
        fileChooser.getExtensionFilters().setAll(getExtensionFilters());
        fileChooser.setSelectedExtensionFilter(getSelectedExtensionFilter());
        File file = fileChooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            setFile(file);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ImagePickerSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return FilePicker.class.getResource("ImagePicker.css").toExternalForm();
    }
}
