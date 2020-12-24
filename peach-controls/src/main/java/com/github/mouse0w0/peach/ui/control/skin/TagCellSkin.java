package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.TagCell;
import com.github.mouse0w0.peach.ui.control.TagView;
import com.github.mouse0w0.peach.ui.control.skin.behavior.TagCellBehavior;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.beans.InvalidationListener;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class TagCellSkin<T> extends LabeledSkinBase<TagCell<T>, BehaviorBase<TagCell<T>>> {
    private final StackPane removeBtn;

    private boolean updatingChildren;

    public TagCellSkin(TagCell<T> cell) {
        super(cell, new TagCellBehavior<>(cell));

        StackPane remove = new StackPane();
        remove.getStyleClass().add("remove");

        removeBtn = new StackPane(remove);
        removeBtn.getStyleClass().add("remove-button");
        removeBtn.setCursor(Cursor.HAND);
        removeBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isPrimaryButtonDown()) {
                TagCell<T> tagCell = getSkinnable();
                if (tagCell.isEditing()) {
                    tagCell.cancelEdit();
                }

                TagView<T> tagView = tagCell.getTagView();
                if (tagView != null) {
                    tagView.fireEvent(new TagView.TagEvent<>(
                            tagView, TagView.removeEvent(), tagCell.getIndex(), tagCell.getItem()));
                }
                event.consume();
            }
        });
        updateChildren0();
        getChildren().addListener((InvalidationListener) observable -> updateChildren0());
    }

    private void updateChildren0() {
        if (updatingChildren) return;
        updatingChildren = true;
        getChildren().add(removeBtn);
        updatingChildren = false;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) + removeBtn.prefWidth(-1);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double prefHeight = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return Math.max(prefHeight, topInset + removeBtn.prefHeight(-1) + bottomInset);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        final double btnWidth = removeBtn.prefWidth(-1);
        final double btnHeight = removeBtn.prefHeight(-1);
        final double contentWidth = w - btnWidth;
        removeBtn.resizeRelocate(x + contentWidth, y, btnWidth, btnHeight);
        layoutLabelInArea(x, y, contentWidth, h, null);
    }
}
