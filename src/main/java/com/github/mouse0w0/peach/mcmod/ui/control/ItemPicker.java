package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ui.dialog.ItemChooser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;

public class ItemPicker extends ItemView {

    public ItemPicker() {
        initialize();
    }

    public ItemPicker(double size) {
        super(size);
        initialize();
    }

    public ItemPicker(double size, boolean enableIgnoreMetadata, boolean enableOreDict) {
        super(size);
        setEnableIgnoreMetadata(enableIgnoreMetadata);
        setEnableOreDict(enableOreDict);
        initialize();
    }

    private void initialize() {
        getStyleClass().add("item-picker");
        setOnDragOver(event -> {
            event.consume();
            if (event.getGestureSource() == event.getTarget()) return;

            IdMetadata item = (IdMetadata) event.getDragboard().getContent(ITEM);
            if (item == null) return;

            event.acceptTransferModes(TransferMode.LINK);
        });
        setOnDragDropped(event -> {
            event.consume();
            setItem((IdMetadata) event.getDragboard().getContent(ITEM));
            event.setDropCompleted(true);
        });
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                setItem(IdMetadata.AIR);
            } else if (event.getClickCount() == 2) {
                show();
            }
        });
    }

    private BooleanProperty enableIgnoreMetadata;

    public final BooleanProperty enableIgnoreMetadataProperty() {
        if (enableIgnoreMetadata == null) {
            enableIgnoreMetadata = new SimpleBooleanProperty(this, "enableIgnoreMetadata", false);
        }
        return enableIgnoreMetadata;
    }

    public final boolean isEnableIgnoreMetadata() {
        return enableIgnoreMetadata != null && enableIgnoreMetadata.get();
    }

    public final void setEnableIgnoreMetadata(boolean enableIgnoreMetadata) {
        enableIgnoreMetadataProperty().set(enableIgnoreMetadata);
    }

    private BooleanProperty enableOreDict;

    public final BooleanProperty enableOreDictProperty() {
        if (enableOreDict == null) {
            enableOreDict = new SimpleBooleanProperty(this, "enableOreDict", false);
        }
        return enableOreDict;
    }

    public final boolean isEnableOreDict() {
        return enableOreDict != null && enableOreDict.get();
    }

    public final void setEnableOreDict(boolean enableOreDict) {
        enableOreDictProperty().set(enableOreDict);
    }

    public void show() {
        setItem(ItemChooser.pick(this, getItem(), isEnableIgnoreMetadata(), isEnableOreDict()));
    }
}
