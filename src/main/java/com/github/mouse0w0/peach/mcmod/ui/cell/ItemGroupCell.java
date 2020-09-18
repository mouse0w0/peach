package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.scene.control.ListCell;

public class ItemGroupCell extends ListCell<ItemGroupData> {

    private ItemView itemView = new ItemView(16, 16);

    public ItemGroupCell() {
        setGraphic(itemView);
    }

    @Override
    protected void updateItem(ItemGroupData item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            itemView.setItem(null);
        } else {
            setText(item.getDisplayName());
            itemView.setItem(item.getItem());
        }
    }
}
