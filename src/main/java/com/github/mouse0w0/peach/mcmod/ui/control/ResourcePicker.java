package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.LowercaseRenameDialog;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ResourcePickerSkin;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResourcePicker extends Control {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcePicker.class);

    public ResourcePicker(Path basePath, String extensionName, String extensionDescription) {
        this.basePath = basePath;
        this.extensionName = extensionName;
        this.extensionFilter = new FileChooser.ExtensionFilter(extensionDescription, "*." + extensionName);

        getStyleClass().setAll("resource-picker");

        setFocusTraversable(false);

        addEventHandler(DragEvent.DRAG_OVER, event -> {
            event.consume();
            if (event.getGestureSource() == event.getTarget()) return;

            Dragboard dragboard = event.getDragboard();
            if (!dragboard.hasFiles()) return;

            List<File> files = dragboard.getFiles();
            if (files.size() != 1) return;

            File file = files.get(0);
            if (!file.getName().endsWith(extensionName)) return;

            event.acceptTransferModes(TransferMode.COPY);
        });
        addEventHandler(DragEvent.DRAG_DROPPED, event -> {
            event.consume();
            setFilePath(event.getDragboard().getFiles().get(0).toPath());
            event.setDropCompleted(true);
        });
    }

    private final Path basePath;

    public Path getBasePath() {
        return basePath;
    }

    private final String extensionName;

    public String getExtensionName() {
        return extensionName;
    }

    private final FileChooser.ExtensionFilter extensionFilter;

    public FileChooser.ExtensionFilter getExtensionFilter() {
        return extensionFilter;
    }

    private ObjectProperty<Path> defaultFilePath;

    public ObjectProperty<Path> defaultFilePathProperty() {
        if (defaultFilePath == null) {
            defaultFilePath = new SimpleObjectProperty<>(this, "defaultFilePath");
        }
        return defaultFilePath;
    }

    public Path getDefaultFilePath() {
        return defaultFilePath != null ? defaultFilePath.get() : null;
    }

    public void setDefaultFilePath(Path defaultFilePath) {
        defaultFilePathProperty().set(defaultFilePath);
    }

    private ObjectProperty<Path> filePath;

    public final ObjectProperty<Path> filePathProperty() {
        if (filePath == null) {
            filePath = new SimpleObjectProperty<Path>(this, "filePath") {
                @Override
                public void set(Path newValue) {
                    if (newValue.startsWith(basePath)) {
                        super.set(newValue);
                    } else {
                        Path targetFilePath = getDefaultFilePath();
                        if (targetFilePath == null) {
                            targetFilePath = basePath.resolve(newValue.getFileName());
                        }

                        if (!Alert.confirm(I18n.translate("dialog.copy.title"),
                                I18n.format("dialog.copy.message.file",
                                        FileUtils.getFileName(newValue), 1, 0, FileUtils.getFileName(targetFilePath.getParent())))) {
                            return;
                        }

                        if (StringUtils.hasUpperCase(FileUtils.getFileName(targetFilePath))) {
                            targetFilePath = LowercaseRenameDialog.create(targetFilePath).showAndWait().orElse(null);
                            if (targetFilePath == null) return;
                        }

                        while (Files.exists(targetFilePath)) {
                            ButtonType buttonType = new Alert(I18n.translate("dialog.paste.title"),
                                    I18n.format("dialog.paste.message", targetFilePath.getParent(), targetFilePath.getFileName()),
                                    PasteDialog.OVERWRITE, PasteDialog.RENAME, ButtonType.CANCEL)
                                    .showAndWait().orElse(ButtonType.CANCEL);
                            if (buttonType == PasteDialog.OVERWRITE) {
                                break;
                            } else if (buttonType == PasteDialog.RENAME) {
                                targetFilePath = LowercaseRenameDialog.create(targetFilePath).showAndWait().orElse(null);
                                if (targetFilePath == null) return;
                            } else {
                                return;
                            }
                        }

                        try {
                            FileUtils.forceCopy(newValue, targetFilePath);
                        } catch (IOException e) {
                            LOGGER.error("An exception has occurred!", e);
                            Alert.error("An exception has occurred!"); // TODO: localize
                            return;
                        }

                        super.set(targetFilePath);
                    }
                }
            };
        }
        return filePath;
    }

    public final Path getFilePath() {
        return filePath != null ? filePath.get() : null;
    }

    public final void setFilePath(Path filePath) {
        filePathProperty().set(filePath);
    }

    public final String getResourcePath() {
        Path filePath = getFilePath();
        if (filePath == null) return null;
        String resourcePath = basePath.relativize(filePath).toString().replace('\\', '/');
        return isHideExtension() ? StringUtils.substringBeforeLast(resourcePath, '.') : resourcePath;
    }

    public final void setResourcePath(String resourcePath) {
        setFilePath(basePath.resolve(isHideExtension() ? resourcePath + "." + extensionName : resourcePath));
    }

    private BooleanProperty hideExtension;

    public final BooleanProperty hideExtensionProperty() {
        if (hideExtension == null) {
            hideExtension = new SimpleBooleanProperty(this, "hideExtension");
        }
        return hideExtension;
    }

    public final boolean isHideExtension() {
        return hideExtension != null && hideExtension.get();
    }

    public final void setHideExtension(boolean hideExtension) {
        hideExtensionProperty().set(hideExtension);
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

    public void showDialog() {
        Window owner = getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getTitle());
        fileChooser.getExtensionFilters().add(getExtensionFilter());
        Path oldFilePath = getFilePath();
        if (oldFilePath != null) {
            fileChooser.setInitialDirectory(oldFilePath.getParent().toFile());
            fileChooser.setInitialFileName(FileUtils.getFileName(oldFilePath));
        } else {
            Path defaultFilePath = getDefaultFilePath();
            if (defaultFilePath != null) {
                fileChooser.setInitialDirectory(defaultFilePath.getParent().toFile());
                fileChooser.setInitialFileName(FileUtils.getFileName(defaultFilePath));
            } else {
                fileChooser.setInitialDirectory(getBasePath().toFile());
            }
        }
        Path newFilePath = FileUtils.toPath(fileChooser.showOpenDialog(owner));
        if (newFilePath == null || newFilePath.equals(oldFilePath)) return;
        setFilePath(newFilePath);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ResourcePickerSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ResourcePicker.class.getResource("ResourcePicker.css").toExternalForm();
    }
}
