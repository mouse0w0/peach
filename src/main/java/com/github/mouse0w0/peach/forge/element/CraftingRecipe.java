package com.github.mouse0w0.peach.forge.element;

import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.ItemStack;
import com.github.mouse0w0.peach.util.ArrayUtils;

public class CraftingRecipe {

    private String id;
    private String namespace;
    private String group;
    private Item[] inputs = ArrayUtils.fill(new Item[9], Item.AIR);
    private ItemStack output = ItemStack.EMPTY;
    private boolean shapeless;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Item[] getInputs() {
        return inputs;
    }

    public void setInputs(Item[] inputs) {
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
