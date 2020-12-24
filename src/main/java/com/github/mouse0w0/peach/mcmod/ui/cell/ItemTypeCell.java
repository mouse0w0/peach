package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.ItemType;
import javafx.scene.control.ListCell;

public class ItemTypeCell extends ListCell<ItemType> {
    @Override
    protected void updateItem(ItemType item, boolean empty) {
        if (empty) {
            setText(null);
        } else {
            setText(item.getLocalizedName());
        }
    }
}
