package com.github.mouse0w0.peach.mcmod;

import java.util.Objects;

public class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(IdMetadata.AIR);

    private IdMetadata item;
    private int amount;

    public ItemStack(IdMetadata item) {
        this(item, 1);
    }

    public ItemStack(IdMetadata item, int amount) {
        setItem(item);
        setAmount(amount);
    }

    public IdMetadata getItem() {
        return item;
    }

    public void setItem(IdMetadata item) {
        if (item == null) {
            throw new NullPointerException("item");
        }
        this.item = item;
    }

    public String getId() {
        return item.getId();
    }

    public int getMetadata() {
        return item.getMetadata();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
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
