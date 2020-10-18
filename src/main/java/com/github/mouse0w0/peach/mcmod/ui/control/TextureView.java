package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.dialog.RenameDialog;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.TextureViewSkin;
import com.github.mouse0w0.peach.mcmod.util.TextureUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TextureView extends Control {

    public TextureView() {
        initialize();
    }

    protected void initialize() {
        getStyleClass().add("texture-view");

        setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(I18n.translate("dialog.texture_chooser.title"));
            Path initialDirectory = TextureUtils.getTexturePath(getProject());
            FileUtils.createDirectoriesIfNotExistsSilently(initialDirectory);
            fileChooser.setInitialDirectory(initialDirectory.toFile());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) copyTextureFile(file.toPath());
        });
        setOnDragOver(event -> {
            event.consume();
            if (event.getGestureSource() == event.getTarget()) return;

            List<File> files = event.getDragboard().getFiles();
            if (files == null || !files.get(0).getName().endsWith(".png")) return;

            event.acceptTransferModes(TransferMode.COPY);
        });
        setOnDragDropped(event -> {
            event.consume();
            event.setDropCompleted(copyTextureFile(event.getDragboard().getFiles().get(0).toPath()));
        });
    }

    private boolean copyTextureFile(Path source) {
        try {
            String fileName = FileUtils.getFileName(source);

            String fileNameWithoutExt;
            if (StringUtils.hasUpperCase(fileName)) {
                RenameDialog dialog = new RenameDialog(String.format(I18n.translate("dialog.rename.message.file"), fileName));
                dialog.getEditor().setText(fileName.toLowerCase());
                dialog.getEditor().requestFocus();
                dialog.getEditor().selectRange(0, fileName.indexOf('.'));
                fileNameWithoutExt = FileUtils.getFileNameWithoutExt(dialog.showAndWait());
            } else {
                fileNameWithoutExt = FileUtils.getFileNameWithoutExt(fileName);
            }

            if (fileNameWithoutExt.isEmpty()) {
                return false;
            }

            String texture = "items/" + fileNameWithoutExt;
            FileUtils.copyIfNotExists(source, TextureUtils.getTextureFile(getProject(), texture));
            setTexture(texture);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    private ObjectProperty<Project> project;

    public final Project getProject() {
        return project == null ? WindowManager.getInstance().getFocusedWindow().getProject() : project.get();
    }

    public final void setProject(Project project) {
        projectProperty().set(project);
    }

    public final ObjectProperty<Project> projectProperty() {
        if (project == null) {
            project = new SimpleObjectProperty<>(this, "project");
        }
        return project;
    }

    private StringProperty texture;

    public final String getTexture() {
        return texture == null ? null : texture.get();
    }

    public final void setTexture(String texture) {
        textureProperty().set(texture);
    }

    public final StringProperty textureProperty() {
        if (texture == null) {
            texture = new SimpleStringProperty(this, "texture");
        }
        return texture;
    }

    private DoubleProperty fitWidth;

    public final void setFitWidth(double value) {
        fitWidthProperty().set(value);
    }

    public final double getFitWidth() {
        return fitWidth == null ? 0 : fitWidth.get();
    }

    public final DoubleProperty fitWidthProperty() {
        if (fitWidth == null) {
            fitWidth = new SimpleDoubleProperty(this, "fitWidth");
        }
        return fitWidth;
    }

    private DoubleProperty fitHeight;

    public final void setFitHeight(double value) {
        fitHeightProperty().set(value);
    }

    public final double getFitHeight() {
        return fitHeight == null ? 0 : fitHeight.get();
    }

    public final DoubleProperty fitHeightProperty() {
        if (fitHeight == null) {
            fitHeight = new SimpleDoubleProperty(this, "fitHeight");
        }
        return fitHeight;
    }

    public final void setFitSize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    @Override
    protected Skin<TextureView> createDefaultSkin() {
        return new TextureViewSkin(this);
    }
}
