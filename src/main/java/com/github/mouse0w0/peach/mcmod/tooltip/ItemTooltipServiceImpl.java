package com.github.mouse0w0.peach.mcmod.tooltip;

import javafx.css.Styleable;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

public final class ItemTooltipServiceImpl implements ItemTooltipService {
    private final Tooltip tooltip;

    public ItemTooltipServiceImpl() {
        tooltip = new Tooltip();
        tooltip.getStyleClass().setAll("item-tooltip");
        tooltip.setOnShowing(event -> {
            Styleable parent = tooltip.getStyleableParent();
            if (parent == null) return;

            ItemTooltipProvider provider = (ItemTooltipProvider) parent;
            List<String> tooltips = new ArrayList<>();
            provider.addToTooltip(tooltips);

            tooltip.setText(String.join("\n", tooltips));
        });
    }

    @Override
    public void install(ItemTooltipProvider provider) {
        Tooltip.install(provider.asNode(), tooltip);
    }

    @Override
    public void uninstall(ItemTooltipProvider provider) {
        Tooltip.uninstall(provider.asNode(), tooltip);
    }
}
