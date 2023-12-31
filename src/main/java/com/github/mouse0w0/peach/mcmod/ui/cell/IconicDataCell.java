package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class IconicDataCell extends ListCell<String> {
    private final Index<String, IconicData> index;
    private final ItemView itemView;

    public static Callback<ListView<String>, ListCell<String>> factory(Index<String, IconicData> index) {
        return $ -> new IconicDataCell(index);
    }

    public static ListCell<String> create(Index<String, IconicData> index) {
        return new IconicDataCell(index);
    }

    private IconicDataCell(Index<String, IconicData> index) {
        this.index = index;
        this.itemView = new ItemView(16);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            IconicData iconicData = index.get(item);
            if (iconicData != null) {
                itemView.setItem(iconicData.getIcon());
                setText(iconicData.getName());
                setGraphic(itemView);
            } else {
                itemView.setItem(IdMetadata.AIR);
                setText(item);
                setGraphic(itemView);
            }
        }
    }
}
