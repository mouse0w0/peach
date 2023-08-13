package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.TagCell;
import com.github.mouse0w0.peach.ui.control.TagView;
import javafx.beans.InvalidationListener;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.control.skin.LabeledSkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class TagCellSkin<T> extends LabeledSkinBase<TagCell<T>> {
    private final StackPane removeButton;

    private boolean updatingChildren;

    public TagCellSkin(TagCell<T> cell) {
        super(cell);

        Region remove = new Region();
        remove.getStyleClass().add("remove");

        removeButton = new StackPane(remove);
        removeButton.getStyleClass().add("remove-button");
        removeButton.setAccessibleRole(AccessibleRole.BUTTON);
        removeButton.setCursor(Cursor.HAND);
        removeButton.setOnMousePressed(event -> {
            if (!event.isPrimaryButtonDown()) return;

            if (cell.isEditing()) {
                cell.cancelEdit();
            }

            TagView<T> tagView = cell.getTagView();
            if (tagView != null) {
                tagView.fireEvent(new TagView.TagEvent<>(tagView, TagView.removeEvent(), cell.getIndex(), cell.getItem()));
            }
            event.consume();
        });
        updateChildren0();
        getChildren().addListener((InvalidationListener) observable -> updateChildren0());
    }

    private void updateChildren0() {
        if (updatingChildren) return;
        updatingChildren = true;
        getChildren().add(removeButton);
        updatingChildren = false;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) + removeButton.prefWidth(-1);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double prefHeight = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return Math.max(prefHeight, topInset + removeButton.prefHeight(-1) + bottomInset);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        final double btnWidth = removeButton.prefWidth(-1);
        final double btnHeight = removeButton.prefHeight(-1);
        final double contentWidth = w - btnWidth;
        removeButton.resizeRelocate(x + contentWidth, y, btnWidth, btnHeight);
        layoutLabelInArea(x, y, contentWidth, h, null);
    }
}
