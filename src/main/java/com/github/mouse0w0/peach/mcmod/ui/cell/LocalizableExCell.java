package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.LocalizableEx;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LocalizableExCell<T extends LocalizableEx> extends ListCell<T> {

    private final ItemView itemView = new ItemView(16, 16);

    public static <T extends LocalizableEx> Callback<ListView<T>, ListCell<T>> factory() {
        return view -> new LocalizableExCell<>();
    }

    public static <T extends LocalizableEx> LocalizableExCell<T> create() {
        return new LocalizableExCell<>();
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.getLocalizedText());
            setGraphic(itemView);
            itemView.setItem(item.getIcon());
        }
    }
}
