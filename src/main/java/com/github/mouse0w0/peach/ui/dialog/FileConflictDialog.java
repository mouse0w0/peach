package com.github.mouse0w0.peach.ui.dialog;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.ui.control.ButtonType;
import javafx.scene.control.ButtonBar;

public class FileConflictDialog extends Alert {
    public static final ButtonType OVERWRITE = ButtonType.localized("dialog.fileConflict.button.overwrite", ButtonBar.ButtonData.OK_DONE);

    public static final ButtonType RENAME = ButtonType.localized("dialog.fileConflict.button.rename", ButtonBar.ButtonData.APPLY);

    public static final ButtonType SKIP = ButtonType.localized("dialog.fileConflict.button.skip", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final ButtonType OVERWRITE_ALL = ButtonType.localized("dialog.fileConflict.button.overwriteAll", ButtonBar.ButtonData.RIGHT);

    public static final ButtonType SKIP_ALL = ButtonType.localized("dialog.fileConflict.button.skipAll", ButtonBar.ButtonData.RIGHT);

    public FileConflictDialog(String folderName, String fileName, boolean multiple) {
        super(AppL10n.localize("dialog.fileConflict.title"), AppL10n.localize("dialog.fileConflict.message", folderName, fileName));
        getButtonBar().setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);
        if (multiple) {
            getButtonTypes().setAll(OVERWRITE, RENAME, SKIP, OVERWRITE_ALL, SKIP_ALL);
        } else {
            getButtonTypes().setAll(OVERWRITE, RENAME, SKIP);
        }
    }
}
