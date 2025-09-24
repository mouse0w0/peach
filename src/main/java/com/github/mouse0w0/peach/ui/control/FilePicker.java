package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.ui.control.skin.FilePickerSkin;
import com.sun.glass.ui.CommonDialogs;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class FilePicker extends Control {
    public enum Type {
        OPEN_FILE, OPEN_DIRECTORY, SAVE_FILE
    }

    private static final EventHandler<DragEvent> ON_DRAG_OVER = event -> {
        if (event.getGestureSource() == event.getTarget()) return;

        Dragboard dragboard = event.getDragboard();
        if (!dragboard.hasFiles()) return;

        List<File> files = dragboard.getFiles();
        if (files.size() != 1) return;

        File file = files.get(0);
        FilePicker filePicker = (FilePicker) event.getSource();
        if (!Utils.testExtensionFilters(file, filePicker.extensionFilters)) return;

        event.acceptTransferModes(TransferMode.COPY);
        event.consume();
    };

    private static final EventHandler<DragEvent> ON_DRAG_DROPPED = event -> {
        FilePicker filePicker = (FilePicker) event.getSource();
        filePicker.setFile(event.getDragboard().getFiles().get(0));
        event.setDropCompleted(true);
        event.consume();
    };

    public FilePicker(Type type) {
        this();
        setType(type);
    }

    public FilePicker() {
        getStyleClass().add("file-picker");
        setFocusTraversable(true);
        addEventHandler(DragEvent.DRAG_OVER, ON_DRAG_OVER);
        addEventHandler(DragEvent.DRAG_DROPPED, ON_DRAG_DROPPED);
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

    public final void setType(Type value) {
        typeProperty().set(value);
    }

    private StringProperty value;

    public final StringProperty valueProperty() {
        if (value == null) {
            value = new SimpleStringProperty(this, "value", "") {
                @Override
                public void set(String newValue) {
                    super.set(newValue != null ? newValue : "");
                }

                @Override
                protected void invalidated() {
                    file = null;
                }
            };
        }
        return value;
    }

    public final String getValue() {
        return value != null ? value.get() : "";
    }

    public final void setValue(String value) {
        valueProperty().set(value);
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

    public final void setConverter(StringConverter<File> value) {
        converterProperty().set(value);
    }

    private File file;

    public final File getFile() {
        if (file != null) {
            return file;
        }
        String value = getValue();
        if (value == null || value.isEmpty()) {
            return null;
        }
        StringConverter<File> converter = getConverter();
        return file = converter != null ? converter.fromString(value) : new File(value);
    }

    public final Path getPath() {
        File file = getFile();
        return file != null ? file.toPath() : null;
    }

    public final void setFile(File value) {
        if (value == null) {
            setValue("");
        } else {
            StringConverter<File> converter = getConverter();
            setValue(converter != null ? converter.toString(value) : value.toString());
            file = value;
        }
    }

    public final void setPath(Path value) {
        setFile(value != null ? value.toFile() : null);
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
            editable = new SimpleBooleanProperty(this, "editable", false);
        }
        return editable;
    }

    public final boolean isEditable() {
        return editable == null || editable.get();
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
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

    public final void setTitle(String value) {
        titleProperty().set(value);
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

    public final void setInitialDirectory(File value) {
        initialDirectoryProperty().set(value);
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

    public final void setInitialFileName(String value) {
        initialFileNameProperty().set(value);
    }

    private ObservableList<FileChooser.ExtensionFilter> extensionFilters;

    public final ObservableList<FileChooser.ExtensionFilter> getExtensionFilters() {
        if (extensionFilters == null) {
            extensionFilters = FXCollections.observableArrayList();
        }
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

    public final void setSelectedExtensionFilter(FileChooser.ExtensionFilter value) {
        selectedExtensionFilterProperty().set(value);
    }

    public void showDialog() {
        File file = switch (getType()) {
            case OPEN_FILE -> showOpenDialog();
            case SAVE_FILE -> showSaveDialog();
            case OPEN_DIRECTORY -> showDirectoryChooser();
        };

        if (file != null) {
            setFile(file);
        }
    }

    private File showOpenDialog() {
        List<File> files = showFileChooser(FileChooserType.OPEN).getFiles();
        return !files.isEmpty() ? files.get(0) : null;
    }

    private File showSaveDialog() {
        List<File> files = showFileChooser(FileChooserType.SAVE).getFiles();
        return !files.isEmpty() ? files.get(0) : null;
    }

    private CommonDialogs.FileChooserResult showFileChooser(FileChooserType type) {
        File file = getFile();

        File initialDirectory;
        String initialFileName;
        if (file != null) {
            initialDirectory = file.getParentFile();
            initialFileName = file.getName();
        } else {
            initialDirectory = getInitialDirectory();
            initialFileName = getInitialFileName();
        }

        return Toolkit.getToolkit().showFileChooser(
                WindowHelper.getPeer(getScene().getWindow()),
                getTitle(),
                initialDirectory,
                initialFileName,
                type,
                extensionFilters != null ? extensionFilters : Collections.emptyList(),
                getSelectedExtensionFilter()
        );
    }

    private File showDirectoryChooser() {
        File file = getFile();

        return Toolkit.getToolkit().showDirectoryChooser(
                WindowHelper.getPeer(getScene().getWindow()),
                getTitle(),
                file != null ? file : getInitialDirectory()
        );
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
