package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.ui.popup.ToolAttributePopup;
import com.github.mouse0w0.peach.ui.control.TagCell;

public class ToolAttributeCell extends TagCell<ToolAttribute> {
    private final ToolAttributePopup popup;

    public ToolAttributeCell(ToolAttributePopup popup) {
        this.popup = popup;
    }

    @Override
    protected void updateItem(ToolAttribute item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(item.toLocalizedText());
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
