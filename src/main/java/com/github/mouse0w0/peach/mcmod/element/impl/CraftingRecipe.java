package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.util.ArrayUtils;

public class CraftingRecipe extends Element {

    private String identifier;
    private String namespace;
    private String group;
    private ItemRef[] inputs = ArrayUtils.fill(new ItemRef[9], ItemRef.AIR);
    private ItemStack output = ItemStack.EMPTY;
    private boolean shapeless;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ItemRef[] getInputs() {
        return inputs;
    }

    public void setInputs(ItemRef[] inputs) {
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
