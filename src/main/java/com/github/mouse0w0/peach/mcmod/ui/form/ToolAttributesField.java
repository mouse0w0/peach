package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.field.Field;
import com.github.mouse0w0.peach.javafx.control.TagView;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.ui.cell.ToolAttributeCell;
import com.github.mouse0w0.peach.mcmod.ui.popup.ToolAttributePopup;
import javafx.scene.Node;

public class ToolAttributesField extends Field {

    public ToolAttribute[] getValue() {
        return getTagView().getItems().toArray(ToolAttribute.EMPTY_ARRAY);
    }

    public void setValue(ToolAttribute[] toolAttributes) {
        getTagView().getItems().setAll(toolAttributes);
    }

    @SuppressWarnings("unchecked")
    public TagView<ToolAttribute> getTagView() {
        return (TagView<ToolAttribute>) getEditor();
    }

    @Override
    protected Node createDefaultEditor() {
        ToolAttributePopup popup = new ToolAttributePopup();
        TagView<ToolAttribute> tagView = new TagView<>();
        tagView.disableProperty().bind(disableProperty());
        tagView.setCellFactory(view -> new ToolAttributeCell(popup));
        tagView.setOnAdd(event -> {
            tagView.getItems().add(event.getIndex(), new ToolAttribute("axe", 0));
            tagView.edit(event.getIndex());
        });
        return tagView;
    }
}
