package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemStackViewSkin;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ItemStackView extends Control {
    private final Project project;

    public ItemStackView(Project project) {
        this.project = project;
        getStyleClass().add("item-stack-view");
    }

    public ItemStackView(Project project, double size) {
        this(project);
        setSize(size);
    }

    public final Project getProject() {
        return project;
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

    private final ObjectProperty<IdMetadata> item = new SimpleObjectProperty<>(this, "item", IdMetadata.air());

    public final ObjectProperty<IdMetadata> itemProperty() {
        return item;
    }

    public final IdMetadata getItem() {
        return item.get();
    }

    public final void setItem(IdMetadata item) {
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
            setItem(IdMetadata.air());
            setAmount(1);
        } else {
            setItem(itemStack.getItem());
            setAmount(itemStack.getAmount());
        }
    }

    public final ItemStack getItemStack() {
        return new ItemStack(getItem(), getAmount());
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
