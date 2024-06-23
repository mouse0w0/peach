package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemStackView;
import com.sun.javafx.scene.control.ListenerHelper;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.text.Font;

public class ItemStackViewSkin extends SkinBase<ItemStackView> {
    private final ItemPicker item;
    private final TextField amount;

    public ItemStackViewSkin(ItemStackView itemStackView) {
        super(itemStackView);

        item = new ItemPicker(itemStackView.getProject());
        item.getStyleClass().setAll("item");
        item.itemProperty().bindBidirectional(itemStackView.itemProperty());
        item.sizeProperty().bind(itemStackView.sizeProperty());

        amount = new TextField();
        amount.getStyleClass().setAll("amount");
        amount.setAlignment(Pos.CENTER_RIGHT);
        amount.setText(Integer.toString(itemStackView.getAmount()));
        // Fix JavaFX pass css font to ContextMenu
        amount.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        amount.fontProperty().bind(itemStackView.sizeProperty()
                .map(size -> new Font("Minecraft", size.doubleValue() / 2)));
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                itemStackView.setAmount(newValue.isEmpty() ? 1 : Integer.parseInt(newValue));
                if (newValue.isEmpty()) return;
                amount.setText(Integer.toString(itemStackView.getAmount()));
            } catch (NumberFormatException e) {
                amount.setText(oldValue);
            }
        });
        amount.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                itemStackView.setAmount(itemStackView.getAmount() + 1);
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                itemStackView.setAmount(itemStackView.getAmount() - 1);
                event.consume();
            }
        });
        amount.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getClickCount() == 2) {
                item.show();
                event.consume();
            }
        });
        amount.addEventHandler(DragEvent.ANY, event -> {
            if (!event.isConsumed()) {
                item.fireEvent(event);
            }
        });

        getChildren().addAll(item, amount);

        ListenerHelper.get(this).addInvalidationListener(itemStackView.amountProperty(), observable -> {
            int newAmount = itemStackView.getAmount();
            if (newAmount == 1 && amount.getText().isEmpty()) return;
            amount.setText(Integer.toString(newAmount));
        });
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + getSkinnable().getSize() + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getSkinnable().getSize() + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + getSkinnable().getSize() + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getSkinnable().getSize() + bottomInset;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + getSkinnable().getSize() + rightInset;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getSkinnable().getSize() + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(item, contentX, contentY, contentWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(amount, contentX - 3, contentY + 4, contentWidth, contentHeight, 0, HPos.RIGHT, VPos.BOTTOM);
    }

    @Override
    public void dispose() {
        getChildren().removeAll(item, amount);

        super.dispose();
    }
}
