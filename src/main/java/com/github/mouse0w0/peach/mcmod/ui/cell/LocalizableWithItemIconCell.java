package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.LocalizableWithItemIcon;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LocalizableWithItemIconCell<K, V extends LocalizableWithItemIcon> extends ListCell<K> {
    private final Index<K, V> index;
    private final ItemView itemView = new ItemView(16);

    public static <K, V extends LocalizableWithItemIcon> Callback<ListView<K>, ListCell<K>> factory(Index<K, V> index) {
        return view -> create(index);
    }

    public static <K, V extends LocalizableWithItemIcon> LocalizableWithItemIconCell<K, V> create(Index<K, V> index) {
        return new LocalizableWithItemIconCell<>(index);
    }

    public LocalizableWithItemIconCell(Index<K, V> index) {
        this.index = index;
    }

    @Override
    protected void updateItem(K item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        }
        V mappedItem = index.get(item);
        if (mappedItem == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(mappedItem.getLocalizedText());
            setGraphic(itemView);
            itemView.setItem(mappedItem.getIcon());
        }
    }
}
