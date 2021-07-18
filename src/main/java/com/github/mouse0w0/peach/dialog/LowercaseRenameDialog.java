package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class LowercaseRenameDialog<T> extends RenameDialog<T> {

    public static RenameDialog<File> create(File file) {
        LowercaseRenameDialog<File> dialog = new LowercaseRenameDialog<>(file.getName(), file.getName().toLowerCase(), file.isDirectory());
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton()
                ? new File(file.getParent(), dialog.getNewName()) : null);
        return dialog;
    }

    public static RenameDialog<File> create(File file, String newName) {
        LowercaseRenameDialog<File> dialog = new LowercaseRenameDialog<>(file.getName(), newName, file.isDirectory());
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton()
                ? new File(file.getParent(), dialog.getNewName()) : null);
        return dialog;
    }

    public static RenameDialog<Path> create(Path path) {
        LowercaseRenameDialog<Path> dialog = new LowercaseRenameDialog<>(FileUtils.getFileName(path), FileUtils.getFileName(path).toLowerCase(), Files.isDirectory(path));
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton() ?
                path.getParent().resolve(dialog.getNewName()) : null);
        return dialog;
    }

    public static RenameDialog<Path> create(Path path, String newName) {
        LowercaseRenameDialog<Path> dialog = new LowercaseRenameDialog<>(FileUtils.getFileName(path), newName, Files.isDirectory(path));
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton() ?
                path.getParent().resolve(dialog.getNewName()) : null);
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
            showMessage(I18n.translate("dialog.rename.error.uppercaseFileName"));
        } else {
            super.setResultAndClose(buttonType, close);
        }
    }
}
