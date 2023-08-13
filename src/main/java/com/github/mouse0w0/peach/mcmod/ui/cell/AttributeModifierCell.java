package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.ui.popup.AttributeModifierPopup;
import com.github.mouse0w0.peach.ui.control.TagCell;

public final class AttributeModifierCell extends TagCell<AttributeModifier> {
    private static final AttributeModifierPopup EDITOR = new AttributeModifierPopup();

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
            EDITOR.edit(this);
        }
    }
}
