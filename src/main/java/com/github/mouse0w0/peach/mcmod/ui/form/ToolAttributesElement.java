package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.ui.cell.ToolAttributeCell;
import com.github.mouse0w0.peach.mcmod.ui.popup.ToolAttributePopup;
import com.github.mouse0w0.peach.ui.control.TagView;
import com.github.mouse0w0.peach.util.Scheduler;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.concurrent.TimeUnit;

public class ToolAttributesElement extends Element {

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
        tagView.setCellFactory(view -> new ToolAttributeCell(popup));
        tagView.setOnAdd(event -> {
            tagView.getItems().add(event.getIndex(), new ToolAttribute("axe", 0));
            Scheduler.computation().schedule(() ->
                    Platform.runLater(() -> tagView.edit(event.getIndex())), 100, TimeUnit.MILLISECONDS);
        });
        return tagView;
    }
}
