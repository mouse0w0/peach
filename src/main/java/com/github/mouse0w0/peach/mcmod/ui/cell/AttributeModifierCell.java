package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.javafx.control.TagCell;
import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.ui.popup.AttributeModifierPopup;

public class AttributeModifierCell extends TagCell<AttributeModifier> {
    private final AttributeModifierPopup popup;

    public AttributeModifierCell(AttributeModifierPopup popup) {
        this.popup = popup;
    }

    @Override
    protected void updateItem(AttributeModifier item, boolean empty) {
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
