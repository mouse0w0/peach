package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.field.ValueField;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class ItemPickerField extends ValueField<Item> {
    private final ObjectProperty<Item> value = new SimpleObjectProperty<>(this, "value");

    @Override
    public ObjectProperty<Item> valueProperty() {
        return value;
    }

    @Override
    public Item getValue() {
        return value.get();
    }

    @Override
    public void setValue(Item value) {
        valueProperty().setValue(value);
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

    public void setFitWidth(double value) {
        getItemPicker().setFitWidth(value);
    }

    public double getFitWidth() {
        return getItemPicker().getFitWidth();
    }

    public DoubleProperty fitWidthProperty() {
        return getItemPicker().fitWidthProperty();
    }

    public void setFitHeight(double value) {
        getItemPicker().setFitHeight(value);
    }

    public double getFitHeight() {
        return getItemPicker().getFitHeight();
    }

    public DoubleProperty fitHeightProperty() {
        return getItemPicker().fitHeightProperty();
    }

    public void setFitSize(double width, double height) {
        getItemPicker().setFitSize(width, height);
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

    public BooleanProperty enableTooltipProperty() {
        return getItemPicker().enableTooltipProperty();
    }

    public boolean isEnableTooltip() {
        return getItemPicker().isEnableTooltip();
    }

    public void setEnableTooltip(boolean enableTooltip) {
        getItemPicker().setEnableTooltip(enableTooltip);
    }

    public ObjectProperty<ContentManager> contentManagerProperty() {
        return getItemPicker().contentManagerProperty();
    }

    public ContentManager getContentManager() {
        return getItemPicker().getContentManager();
    }

    public void setContentManager(ContentManager contentManager) {
        getItemPicker().setContentManager(contentManager);
    }

    public ItemPicker getItemPicker() {
        return (ItemPicker) getEditor();
    }

    @Override
    protected Node createDefaultEditor() {
        ItemPicker itemPicker = new ItemPicker();
        itemPicker.itemProperty().bindBidirectional(valueProperty());
        itemPicker.maxWidthProperty().bind(itemPicker.fitWidthProperty());
        itemPicker.maxHeightProperty().bind(itemPicker.fitHeightProperty());
        return itemPicker;
    }
}
