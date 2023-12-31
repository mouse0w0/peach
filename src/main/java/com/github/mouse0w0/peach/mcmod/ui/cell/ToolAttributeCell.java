package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.popup.ToolAttributePopup;
import com.github.mouse0w0.peach.ui.control.TagCell;

public final class ToolAttributeCell extends TagCell<ToolAttribute> {
    private final Index<String, GameData> toolTypeIndex;
    private final ToolAttributePopup popup;

    public ToolAttributeCell(Index<String, GameData> toolTypeIndex) {
        this.toolTypeIndex = toolTypeIndex;
        this.popup = new ToolAttributePopup(toolTypeIndex);
    }

    @Override
    protected void updateItem(ToolAttribute item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(item.toLocalizedText(toolTypeIndex));
        }
    }

    @Override
    public void startEdit() {
        if (isEditing()) return;

        super.startEdit();

        if (isEditing()) {
            popup.edit(this);
        }
    }
}
