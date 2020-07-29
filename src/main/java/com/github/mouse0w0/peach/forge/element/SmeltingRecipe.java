package com.github.mouse0w0.peach.forge.element;

import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.ItemStack;

public class SmeltingRecipe {

    private Item input;
    private ItemStack output;
    private double xp;

    public Item getInput() {
        return input;
    }

    public void setInput(Item input) {
        this.input = input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }
}
