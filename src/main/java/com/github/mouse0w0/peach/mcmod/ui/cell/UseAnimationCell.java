package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.UseAnimation;
import javafx.scene.control.ListCell;

public class UseAnimationCell extends ListCell<UseAnimation> {
    @Override
    protected void updateItem(UseAnimation item, boolean empty) {
        if (empty) {
            setText(null);
        } else {
            setText(item.getLocalizedName());
        }
    }
}
