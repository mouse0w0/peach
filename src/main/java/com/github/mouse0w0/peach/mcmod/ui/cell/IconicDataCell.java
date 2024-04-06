package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class IconicDataCell extends ListCell<String> {
    private final Index<String, IconicData> index;
    private final ItemView itemView;

    public static Callback<ListView<String>, ListCell<String>> factory(Project project, Index<String, IconicData> index) {
        return $ -> new IconicDataCell(project, index);
    }

    public static ListCell<String> create(Project project, Index<String, IconicData> index) {
        return new IconicDataCell(project, index);
    }

    private IconicDataCell(Project project, Index<String, IconicData> index) {
        this.index = index;
        this.itemView = new ItemView(project, 16);
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
