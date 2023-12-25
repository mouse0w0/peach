package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.ui.form.field.ValueField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class ItemPickerField extends ValueField<IdMetadata> {
    private final ObjectProperty<IdMetadata> value = new SimpleObjectProperty<>(this, "value");

    @Override
    public ObjectProperty<IdMetadata> valueProperty() {
        return value;
    }

    @Override
    public IdMetadata getValue() {
        return value.get();
    }

    @Override
    public void setValue(IdMetadata value) {
        this.value.set(value);
    }

    public BooleanProperty enableIgnoreMetadataProperty() {
        return getItemPicker().enableIgnoreMetadataProperty();
    }

    public boolean isEnableIgnoreMetadata() {
        return getItemPicker().isEnableIgnoreMetadata();
    }

    public void setEnableIgnoreMetadata(boolean enableIgnoreMetadata) {
        getItemPicker().setEnableIgnoreMetadata(enableIgnoreMetadata);
    }

    public BooleanProperty enableOreDictProperty() {
        return getItemPicker().enableOreDictProperty();
    }

    public boolean isEnableOreDict() {
        return getItemPicker().isEnableOreDict();
    }

    public void setEnableOreDict(boolean enableOreDict) {
        getItemPicker().setEnableOreDict(enableOreDict);
    }

    public DoubleProperty sizeProperty() {
        return getItemPicker().sizeProperty();
    }

    public double getSize() {
        return getItemPicker().getSize();
    }

    public void setSize(double value) {
        getItemPicker().setSize(value);
    }

    public BooleanProperty playAnimationProperty() {
        return getItemPicker().playAnimationProperty();
    }

    public boolean isPlayAnimation() {
        return getItemPicker().isPlayAnimation();
    }

    public void setPlayAnimation(boolean playAnimation) {
        getItemPicker().setPlayAnimation(playAnimation);
    }

    public ItemPicker getItemPicker() {
        return (ItemPicker) getEditorNode();
    }

    @Override
    protected Node createEditorNode() {
        ItemPicker itemPicker = new ItemPicker();
        itemPicker.getStyleClass().add("minecraft-small-slot-32x");
        itemPicker.itemProperty().bindBidirectional(valueProperty());
        itemPicker.disableProperty().bind(disableProperty());
        return itemPicker;
    }
}
