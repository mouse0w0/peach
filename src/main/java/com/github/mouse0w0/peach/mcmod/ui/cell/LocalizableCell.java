package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.Localizable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LocalizableCell<T extends Localizable> extends ListCell<T> {

    public static <T extends Localizable> Callback<ListView<T>, ListCell<T>> factory() {
        return view -> new LocalizableCell<>();
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item.getLocalizedText());
        }
    }
}
