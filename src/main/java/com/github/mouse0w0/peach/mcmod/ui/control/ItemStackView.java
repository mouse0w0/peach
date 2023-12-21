package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemStackViewSkin;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.List;

public class ItemStackView extends Control {

    public ItemStackView() {
        getStyleClass().setAll("item-stack-view");
    }

    public ItemStackView(double size) {
        this();
        setSize(size);
    }

    private final DoubleProperty size = new SimpleDoubleProperty(this, "size", 16);

    public final DoubleProperty sizeProperty() {
        return size;
    }

    public final double getSize() {
        return size.get();
    }

    public final void setSize(double value) {
        size.set(value);
    }

    private final ObjectProperty<ItemRef> item = new SimpleObjectProperty<>(this, "item", ItemRef.AIR);

    public final ObjectProperty<ItemRef> itemProperty() {
        return item;
    }

    public final ItemRef getItem() {
        return item.get();
    }

    public final void setItem(ItemRef item) {
        this.item.set(item);
    }

    private IntegerProperty amount;

    public final IntegerProperty amountProperty() {
        if (amount == null) {
            amount = new SimpleIntegerProperty(this, "amount", 1) {
                @Override
                public void set(int newValue) {
                    super.set(Math.min(Math.max(newValue, 1), 127));
                }
            };
        }
        return amount;
    }

    public final int getAmount() {
        return amount == null ? 1 : amount.get();
    }

    public final void setAmount(int amount) {
        amountProperty().set(amount);
    }

    public final void setItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            setItem(ItemRef.AIR);
            setAmount(1);
        } else {
            setItem(itemStack.getItem());
            setAmount(itemStack.getAmount());
        }
    }

    public final ItemStack getItemStack() {
        return new ItemStack(getItem(), getAmount());
    }

    private ObjectProperty<Index<ItemRef, List<Item>>> index;

    public final ObjectProperty<Index<ItemRef, List<Item>>> indexProperty() {
        if (index == null) {
            index = new SimpleObjectProperty<>(this, "index", getDefaultIndex());
        }
        return index;
    }

    public final Index<ItemRef, List<Item>> getIndex() {
        return indexProperty().get();
    }

    public final void setIndex(Index<ItemRef, List<Item>> index) {
        indexProperty().set(index);
    }

    private Index<ItemRef, List<Item>> getDefaultIndex() {
        Project project = WindowManager.getInstance().getFocusedProject();
        if (project == null) return null;
        return IndexManager.getInstance(project).getIndex(IndexTypes.ITEM);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemStackViewSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ItemStackView.class.getResource("ItemStackView.css").toExternalForm();
    }
}
