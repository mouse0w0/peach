package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import javafx.scene.input.Clipboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class PasteAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasteAction.class);

    @Override
    public void update(ActionEvent event) {
        getAppearance().setDisable(!Clipboard.getSystemClipboard().hasFiles());
    }

    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        Path folder = Files.isDirectory(path) ? path : path.getParent();

        List<File> files = Clipboard.getSystemClipboard().getFiles();
        boolean multiple = files.size() > 1;
        boolean overwriteAll = false;
        boolean skipAll = false;
        for (File file : files) {
            Path source = file.toPath();
            Path target = folder.resolve(file.getName());
            try {
                if (Files.exists(target)) {
                    if (skipAll) continue;
                    if (!overwriteAll) {
                        ButtonType buttonType = new PasteDialog(I18n.translate("dialog.paste.title"),
                                I18n.format("dialog.paste.message", folder, file.getName()), multiple)
                                .showAndWait().orElse(PasteDialog.SKIP);
                        if (buttonType == PasteDialog.SKIP) {
                            continue;
                        } else if (buttonType == PasteDialog.SKIP_ALL) {
                            skipAll = true;
                            continue;
                        } else if (buttonType == PasteDialog.OVERWRITE_ALL) {
                            overwriteAll = true;
                        }
                    }
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.copy(source, target);
                }
            } catch (IOException e) {
                LOGGER.error("Failed to paste file because an exception has occurred.", e);
                Alert.error(I18n.translate("dialog.paste.title"), e.toString());
            }
        }
    }
}
