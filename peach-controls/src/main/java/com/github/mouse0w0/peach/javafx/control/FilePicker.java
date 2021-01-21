package com.github.mouse0w0.peach.javafx.control;

import com.github.mouse0w0.peach.javafx.control.skin.FilePickerSkin;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class FilePicker extends Control {

    public enum Type {
        OPEN_FILE,
        OPEN_DIRECTORY,
        SAVE_FILE
    }

    public FilePicker() {
        getStyleClass().setAll("file-picker");

        setFocusTraversable(false);

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
    }

    // Properties
    private StringProperty text;

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text", "") {
                @Override
                public void set(String newValue) {
                    super.set(newValue != null ? newValue : "");
                }
            };
        }
        return text;
    }

    public final String getText() {
        return text != null ? text.get() : "";
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    private ObjectProperty<Type> type;

    public final ObjectProperty<Type> typeProperty() {
        if (type == null) {
            type = new SimpleObjectProperty<>(this, "type", Type.OPEN_FILE);
        }
        return type;
    }

    public final Type getType() {
        return type != null ? type.get() : Type.OPEN_FILE;
    }

    public final void setType(Type type) {
        typeProperty().set(type);
    }

    private ObjectProperty<StringConverter<File>> converter;

    public final ObjectProperty<StringConverter<File>> converterProperty() {
        if (converter == null) {
            converter = new SimpleObjectProperty<>(this, "converter");
        }
        return converter;
    }

    public final StringConverter<File> getConverter() {
        return converter != null ? converter.get() : null;
    }

    public final void setConverter(StringConverter<File> converter) {
        converterProperty().set(converter);
    }

    public final File toFile() {
        StringConverter<File> converter = getConverter();
        String text = getText();
        if (text.isEmpty()) return null;
        return converter != null ? converter.fromString(text) : new File(text);
    }

    public final Path toPath() {
        File file = toFile();
        return file != null ? file.toPath() : null;
    }

    public final void setFile(File file) {
        if (file == null) {
            setText("");
        } else {
            StringConverter<File> converter = getConverter();
            setText(converter != null ? converter.toString(file) : file.getAbsolutePath());
        }
    }

    public final void setPath(Path path) {
        setFile(path.toFile());
    }

    private StringProperty promptText;

    public final StringProperty promptTextProperty() {
        if (promptText == null) {
            promptText = new SimpleStringProperty(this, "promptText", "");
        }
        return promptText;
    }

    public final String getPromptText() {
        return promptText != null ? promptText.get() : "";
    }

    public final void setPromptText(String value) {
        promptTextProperty().set(value);
    }

    private BooleanProperty editable;

    public final BooleanProperty editableProperty() {
        if (editable == null) {
            editable = new SimpleBooleanProperty(this, "editable", true);
        }
        return editable;
    }

    public final boolean isEditable() {
        return editable == null || editable.get();
    }

    public final void setEditable(boolean editable) {
        editableProperty().set(editable);
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

    public void showDialog() {
        FilePicker.Type type = getType();
        File oldFile = toFile();
        Window owner = getScene().getWindow();
        File file = null;
        if (type == FilePicker.Type.OPEN_DIRECTORY) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(getTitle());
            if (oldFile == null) {
                File initialDirectory = getInitialDirectory();
                if (initialDirectory == null || initialDirectory.isDirectory()) {
                    directoryChooser.setInitialDirectory(getInitialDirectory());
                }
            } else {
                directoryChooser.setInitialDirectory(oldFile.getParentFile());
            }
            file = directoryChooser.showDialog(owner);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(getTitle());
            if (oldFile == null) {
                File initialDirectory = getInitialDirectory();
                if (initialDirectory == null || initialDirectory.isDirectory()) {
                    fileChooser.setInitialDirectory(initialDirectory);
                }
                fileChooser.setInitialFileName(getInitialFileName());
            } else {
                fileChooser.setInitialDirectory(oldFile.getParentFile());
                fileChooser.setInitialFileName(oldFile.getName());
            }
            fileChooser.getExtensionFilters().setAll(getExtensionFilters());
            fileChooser.setSelectedExtensionFilter(getSelectedExtensionFilter());

            if (type == FilePicker.Type.OPEN_FILE) {
                file = fileChooser.showOpenDialog(owner);
            } else if (type == FilePicker.Type.SAVE_FILE) {
                file = fileChooser.showSaveDialog(owner);
            }
        }

        if (file != null) {
            setFile(file);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FilePickerSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return FilePicker.class.getResource("FilePicker.css").toExternalForm();
    }
}
