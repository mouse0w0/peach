package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import com.github.mouse0w0.peach.dialog.RenameDialogWithValidator;
import com.github.mouse0w0.peach.javafx.Check;
import com.github.mouse0w0.peach.javafx.util.NotificationLevel;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtils {
    public static final String LANG = "resources/lang";

    public static final String BLOCK_STATES = "resources/blockstates";

    public static final String MODELS = "resources/models";
    public static final String BLOCK_MODELS = MODELS + "/blocks";
    public static final String ITEM_MODELS = MODELS + "/items";

    public static final String TEXTURES = "resources/textures";
    public static final String BLOCK_TEXTURES = TEXTURES + "/blocks";
    public static final String ITEM_TEXTURES = TEXTURES + "/items";
    public static final String ARMOR_TEXTURES = TEXTURES + "/armors";
    public static final String GUI_TEXTURES = TEXTURES + "/gui";

    public static Path getResourcePath(Project project, String resource) {
        return project.getPath().resolve(resource);
    }

    public static Path getTextureFile(Project project, String textureName) {
        return project.getPath().resolve(ResourceUtils.TEXTURES + "/" + textureName + ".png");
    }

    public static String relativize(Path path, Path other) {
        return path.relativize(other).toString().replace('\\', '/');
    }

    public static InputStream getMissingTexture() {
        return ResourceUtils.class.getResourceAsStream("/image/mcmod/missing.png");
    }

    public static Path copyToLowerCaseFile(Path source, Path target) throws IOException {
        while (true) {
            String fileName = target.getFileName().toString();
            if (StringUtils.hasUpperCase(fileName)) {
                target = new RenameDialogWithValidator(target, fileName.toLowerCase(),
                        new Check<>(s -> !StringUtils.hasUpperCase(s), NotificationLevel.ERROR, I18n.translate("")))
                        .showAndWait().orElse(null);
                if (target == null) return null;
            } else if (Files.exists(target)) {
                ButtonType buttonType = new Alert(I18n.translate("dialog.paste.title"),
                        I18n.format("dialog.paste.message", target.getParent(), target.getFileName()),
                        PasteDialog.OVERWRITE, PasteDialog.RENAME, ButtonType.CANCEL)
                        .showAndWait().orElse(ButtonType.CANCEL);
                if (buttonType == PasteDialog.OVERWRITE) {
                    return FileUtils.forceCopy(source, target);
                } else if (buttonType == PasteDialog.RENAME) {
                    target = new RenameDialogWithValidator(target, fileName.toLowerCase(),
                            new Check<>(s -> !StringUtils.hasUpperCase(s), NotificationLevel.ERROR, I18n.translate("")))
                            .showAndWait().orElse(null);
                    if (target == null) return null;
                } else {
                    return null;
                }
            } else {
                return FileUtils.copySafely(source, target);
            }
        }
    }

    private ResourceUtils() {
    }
}
