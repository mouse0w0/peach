package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemStackViewSkin;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ItemStackView extends Control {

    public ItemStackView() {
        getStyleClass().setAll("item-stack-view");
    }

    private DoubleProperty fitWidth;

    public final void setFitWidth(double value) {
        fitWidthProperty().set(value);
    }

    public final double getFitWidth() {
        return fitWidth == null ? 0 : fitWidth.get();
    }

    public final DoubleProperty fitWidthProperty() {
        if (fitWidth == null) {
            fitWidth = new SimpleDoubleProperty(this, "fitWidth");
        }
        return fitWidth;
    }

    private DoubleProperty fitHeight;

    public final void setFitHeight(double value) {
        fitHeightProperty().set(value);
    }

    public final double getFitHeight() {
        return fitHeight == null ? 0 : fitHeight.get();
    }

    public final DoubleProperty fitHeightProperty() {
        if (fitHeight == null) {
            fitHeight = new SimpleDoubleProperty(this, "fitHeight");
        }
        return fitHeight;
    }

    public final void setFitSize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    private final ObjectProperty<Item> item = new SimpleObjectProperty<>(this, "item", Item.AIR);

    public final ObjectProperty<Item> itemProperty() {
        return item;
    }

    public final Item getItem() {
        return item.get();
    }

    public final void setItem(Item item) {
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
            setItem(Item.AIR);
            setAmount(1);
        } else {
            setItem(itemStack.getItem());
            setAmount(itemStack.getAmount());
        }
    }

    public final ItemStack getItemStack() {
        return new ItemStack(getItem(), getAmount());
    }

    private ObjectProperty<ContentManager> contentManager;

    public final ObjectProperty<ContentManager> contentManagerProperty() {
        if (contentManager == null) {
            Project project = WindowManager.getInstance().getFocusedProject();
            contentManager = new SimpleObjectProperty<>(this, "contentManager",
                    project != null ? ContentManager.getInstance(project) : null);
        }
        return contentManager;
    }

    public final ContentManager getContentManager() {
        return contentManager == null ?
                ContentManager.getInstance(WindowManager.getInstance().getFocusedProject()) :
                contentManager.get();
    }

    public final void setContentManager(ContentManager contentManager) {
        contentManagerProperty().set(contentManager);
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
