package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemStackView;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ItemStackViewSkin extends SkinBase<ItemStackView> {

    private final ItemPicker item;
    private final TextField amount;

    public ItemStackViewSkin(ItemStackView control) {
        super(control);

        item = new ItemPicker();
        item.getStyleClass().setAll("item");
        item.itemProperty().bindBidirectional(control.itemProperty());
        item.fitWidthProperty().bind(control.fitWidthProperty());
        item.fitHeightProperty().bind(control.fitHeightProperty());
        item.contentManagerProperty().bind(control.contentManagerProperty());
        item.maxWidthProperty().bind(control.fitWidthProperty());
        item.maxHeightProperty().bind(control.fitHeightProperty());

        amount = new TextField();
        amount.getStyleClass().setAll("amount");
        amount.setAlignment(Pos.CENTER_RIGHT);
        amount.setText(Integer.toString(control.getAmount()));
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                control.setAmount(newValue.isEmpty() ? 1 : Integer.parseInt(newValue));
                if (newValue.isEmpty()) return;
                amount.setText(Integer.toString(control.getAmount()));
            } catch (NumberFormatException e) {
                amount.setText(oldValue);
            }
        });
        amount.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                control.setAmount(control.getAmount() + 1);
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                control.setAmount(control.getAmount() - 1);
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

        control.amountProperty().addListener(observable -> {
            int newAmount = control.getAmount();
            if (newAmount == 1 && amount.getText().isEmpty()) return;
            amount.setText(Integer.toString(newAmount));
        });
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(item, contentX, contentY, contentWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(amount, contentX, contentY, contentWidth, contentHeight, 0, HPos.RIGHT, VPos.BOTTOM);
    }
}
