package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.ui.util.Alerts;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Path;
import java.util.List;

public class DeleteAction extends Action {
    @Override
    @SuppressWarnings("unchecked")
    public void perform(ActionEvent event) {
        List<?> items = DataKeys.SELECTED_ITEMS.get(event);
        if (items == null || items.isEmpty()) return;

        if (!(items.get(0) instanceof Path)) return;

        List<Path> paths = (List<Path>) items;

        String message;
        if (paths.size() == 1) {
            message = String.format(I18n.translate("dialog.delete.message0"), paths.get(0).getFileName());
        } else {
            message = String.format(I18n.translate("dialog.delete.message1"), paths.get(0).getFileName(), paths.size());
        }

        if (!Alerts.confirm(message)) return;

        for (Path path : paths) {
            FileUtils.delete(path);
        }
    }
}
