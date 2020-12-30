package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.ui.cell.AttributeModifierCell;
import com.github.mouse0w0.peach.mcmod.ui.popup.AttributeModifierPopup;
import com.github.mouse0w0.peach.ui.control.TagView;
import com.github.mouse0w0.peach.util.Scheduler;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.concurrent.TimeUnit;

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
                    new AttributeModifier("generic.maxHealth", 0, AttributeModifier.Operation.ADD));
            Scheduler.computation().schedule(() ->
                    Platform.runLater(() -> tagView.edit(event.getIndex())), 100, TimeUnit.MILLISECONDS);
        });
        return tagView;
    }
}
