package com.github.mouse0w0.peach.mcmod.tooltip;

import javafx.scene.Node;

import java.util.List;

public interface ItemTooltipProvider {
    void addToTooltip(List<String> tooltips);

    default Node asNode() {
        return (Node) this;
    }
}
