package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.util.ArrayUtils;

public class CraftingElement extends Element {
    private String identifier;
    private String group;
    private IdMetadata[] inputs = ArrayUtils.fill(new IdMetadata[9], IdMetadata.air());
    private ItemStack output = ItemStack.EMPTY;
    private boolean shapeless;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public IdMetadata[] getInputs() {
        return inputs;
    }

    public void setInputs(IdMetadata[] inputs) {
        this.inputs = inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    public boolean isShapeless() {
        return shapeless;
    }

    public void setShapeless(boolean shapeless) {
        this.shapeless = shapeless;
    }
}
