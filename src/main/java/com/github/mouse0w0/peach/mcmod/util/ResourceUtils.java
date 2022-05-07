package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.LowercaseRenameDialog;
import com.github.mouse0w0.peach.dialog.PasteDialog;
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
    public static final String BLOCK_MODELS = MODELS + "/block";
    public static final String ITEM_MODELS = MODELS + "/item";

    public static final String TEXTURES = "resources/textures";
    public static final String BLOCK_TEXTURES = TEXTURES + "/block";
    public static final String ITEM_TEXTURES = TEXTURES + "/item";
    public static final String ARMOR_TEXTURES = TEXTURES + "/armor";
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
                target = LowercaseRenameDialog.create(target, fileName.toLowerCase()).showAndWait().orElse(null);
                if (target == null) return null;
            } else if (Files.exists(target)) {
                ButtonType buttonType = new PasteDialog(I18n.translate("dialog.paste.title"),
                        I18n.format("dialog.paste.error.existsSameFile",
                                FileUtils.getFileName(target.getParent()), FileUtils.getFileName(target)),
                        false).showAndWait().orElse(PasteDialog.SKIP);
                if (buttonType == PasteDialog.OVERWRITE) {
                    return FileUtils.forceCopy(source, target);
                } else if (buttonType == PasteDialog.RENAME) {
                    target = LowercaseRenameDialog.create(target, fileName.toLowerCase()).showAndWait().orElse(null);
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
