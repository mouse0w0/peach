package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.javafx.control.TagView;
import com.github.mouse0w0.peach.mcmod.Attribute;
import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.ui.cell.AttributeModifierCell;
import com.github.mouse0w0.peach.mcmod.ui.popup.AttributeModifierPopup;
import javafx.scene.Node;

public class AttributeModifiersField extends Element {

    public AttributeModifier[] getValue() {
        return getTagView().getItems().toArray(AttributeModifier.EMPTY_ARRAY);
    }

    public void setValue(AttributeModifier[] attributeModifiers) {
        getTagView().getItems().setAll(attributeModifiers);
    }

    @SuppressWarnings("unchecked")
    public TagView<AttributeModifier> getTagView() {
        return (TagView<AttributeModifier>) getEditor();
    }

    @Override
    protected Node createDefaultEditor() {
        AttributeModifierPopup popup = new AttributeModifierPopup();
        TagView<AttributeModifier> tagView = new TagView<>();
        tagView.setCellFactory(view -> new AttributeModifierCell(popup));
        tagView.setOnAdd(event -> {
            tagView.getItems().add(event.getIndex(),
                    new AttributeModifier(Attribute.MAX_HEALTH, 0, AttributeModifier.Operation.ADD));
            tagView.edit(event.getIndex());
        });
        return tagView;
    }
}
