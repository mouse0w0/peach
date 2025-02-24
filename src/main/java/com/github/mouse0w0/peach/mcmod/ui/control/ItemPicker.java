package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.tooltip.ItemTooltipService;
import com.github.mouse0w0.peach.mcmod.ui.dialog.ItemChooser;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ItemPicker extends ItemView {
    public ItemPicker(Project project) {
        super(project);
        initialize();
    }

    public ItemPicker(Project project, double size) {
        super(project, size);
        initialize();
    }

    public ItemPicker(Project project, double size, boolean enableIgnoreMetadata, boolean enableOreDict) {
        super(project, size);
        setEnableIgnoreMetadata(enableIgnoreMetadata);
        setEnableOreDict(enableOreDict);
        initialize();
    }

    private static final EventHandler<MouseEvent> ON_MOUSE_CLICKED = event -> {
        ItemPicker itemPicker = (ItemPicker) event.getSource();
        if (event.getButton() == MouseButton.MIDDLE) {
            itemPicker.setItem(IdMetadata.air());
        } else if (event.getClickCount() == 2) {
            itemPicker.show();
        }
    };

    private void initialize() {
        getStyleClass().add("item-picker");
        setOnMouseClicked(ON_MOUSE_CLICKED);
        ItemTooltipService.getInstance(getProject()).install(this);
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
