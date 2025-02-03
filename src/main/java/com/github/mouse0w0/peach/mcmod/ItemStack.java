package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.util.Validate;

public class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(IdMetadata.air());

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
        this.item = Validate.notNull(item);
    }

    public Identifier getIdentifier() {
        return item.getIdentifier();
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
        return amount <= 0 || item.isAir();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ItemStack that = (ItemStack) o;
        return amount == that.amount && item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode() * 31 + amount;
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "item=" + item +
                ", amount=" + amount +
                '}';
    }
}
