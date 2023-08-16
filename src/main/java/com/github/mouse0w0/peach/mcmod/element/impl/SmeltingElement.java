package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.Element;

public class SmeltingElement extends Element {

    private ItemRef input;
    private ItemStack output;
    private double xp;

    public ItemRef getInput() {
        return input;
    }

    public void setInput(ItemRef input) {
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
