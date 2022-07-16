package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.LocalizableWithItemIcon;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LocalizableWithItemIconCell<T extends LocalizableWithItemIcon> extends ListCell<T> {

    private final ItemView itemView = new ItemView(16, 16);

    public static <T extends LocalizableWithItemIcon> Callback<ListView<T>, ListCell<T>> factory() {
        return view -> new LocalizableWithItemIconCell<>();
    }

    public static <T extends LocalizableWithItemIcon> LocalizableWithItemIconCell<T> create() {
        return new LocalizableWithItemIconCell<>();
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
