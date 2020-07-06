package com.github.mouse0w0.peach.forge;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class ItemStack {

    private ItemToken item;
    private int amount;

    public ItemStack(ItemToken item) {
        this(item, 1);
    }

    public ItemStack(ItemToken item, int amount) {
        this.item = Validate.notNull(item);
        this.amount = amount;
    }

    public ItemToken getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStack itemStack = (ItemStack) o;
        return amount == itemStack.amount &&
                item.equals(itemStack.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, amount);
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "item=" + item +
                ", amount=" + amount +
                '}';
    }
}
