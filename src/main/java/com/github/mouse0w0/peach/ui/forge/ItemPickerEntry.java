package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.contentPack.data.ItemData;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import org.apache.commons.lang3.Validate;

import java.util.List;

class ItemPickerEntry extends ToggleButton {

    private static final Tooltip TOOLTIP;

    private final Item item;
    private final ItemView itemView;

    static {
        TOOLTIP = new Tooltip();
        TOOLTIP.setFont(Font.font(13));
        TOOLTIP.setOnShowing(event ->
                FXUtils.getTooltipOwnerNode().ifPresent(node -> {
                            ItemPickerEntry entry = (ItemPickerEntry) node;
                            Item item = entry.getItem();
                            List<ItemData> itemData = entry.getItemData();
                            StringBuilder sb = new StringBuilder();

                            sb.append(item.getId()).append("\n--------------------\n");

                            for (ItemData itemDatum : itemData) {
                                sb.append(itemDatum.getDisplayName()).append("\n");
                            }

                            TOOLTIP.setText(sb.substring(0, sb.length() - 1));
                        }
                ));
    }

    public ItemPickerEntry(Item item, ToggleGroup toggleGroup) {
        this.item = Validate.notNull(item);
        itemView = new ItemView(item, 32, 32);
        getStyleClass().add("entry");
        setToggleGroup(toggleGroup);
        setGraphic(itemView);
        setTooltip(TOOLTIP);
        FXUtils.setFixedSize(this, 32, 32);
    }

    public Item getItem() {
        return item;
    }

    public List<ItemData> getItemData() {
        return itemView.getItemData();
    }

    @Override
    public void fire() {
        // we don't toggle from selected to not selected if part of a group
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }
}
