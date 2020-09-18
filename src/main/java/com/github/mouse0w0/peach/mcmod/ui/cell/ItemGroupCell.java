package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.scene.control.ListCell;

public class ItemGroupCell extends ListCell<ItemGroupData> {

    @Override
    protected void updateItem(ItemGroupData item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.getDisplayName());
            setGraphic(new ItemView(item.getItem(), 16, 16));
        }
    }
}
