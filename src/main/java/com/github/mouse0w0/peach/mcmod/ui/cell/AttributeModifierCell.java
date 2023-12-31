package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.popup.AttributeModifierPopup;
import com.github.mouse0w0.peach.ui.control.TagCell;

public final class AttributeModifierCell extends TagCell<AttributeModifier> {
    private final Index<String, GameData> attributeIndex;
    private final AttributeModifierPopup popup;

    public AttributeModifierCell(Index<String, GameData> attributeIndex) {
        this.attributeIndex = attributeIndex;
        this.popup = new AttributeModifierPopup(attributeIndex);
    }

    @Override
    protected void updateItem(AttributeModifier item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(item.toLocalizedText(attributeIndex));
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
