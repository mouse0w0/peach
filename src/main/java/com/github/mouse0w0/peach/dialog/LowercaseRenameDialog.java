package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.javafx.control.ButtonType;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class LowercaseRenameDialog<T> extends RenameDialog<T> {

    public static RenameDialog<Path> create(Path path) {
        return create(path, FileUtils.getFileName(path).toLowerCase());
    }

    public static RenameDialog<Path> create(Path path, String newName) {
        LowercaseRenameDialog<Path> dialog = new LowercaseRenameDialog<>(FileUtils.getFileName(path), newName, Files.isDirectory(path));
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton() ?
                path.getParent().resolve(dialog.getNewName()) : null);
        return dialog;
    }

    public static RenameDialog<File> create(File file) {
        return create(file, file.getName().toLowerCase());
    }

    public static RenameDialog<File> create(File file, String newName) {
        LowercaseRenameDialog<File> dialog = new LowercaseRenameDialog<>(file.getName(), newName, file.isDirectory());
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton()
                ? new File(file.getParent(), dialog.getNewName()) : null);
        return dialog;
    }

    public LowercaseRenameDialog(String rawName, String newName, boolean isDirectory) {
        super(rawName, newName, isDirectory);
    }

    @Override
    protected void setResultAndClose(ButtonType buttonType, boolean close) {
        if (buttonType == null || buttonType.getButtonData().isCancelButton()) {
            super.setResultAndClose(buttonType, close);
        } else if (StringUtils.hasUpperCase(getNewName())) {
            showMessage(AppL10n.localize("dialog.rename.error.lowercaseFileName"));
        } else {
            super.setResultAndClose(buttonType, close);
        }
    }
}
