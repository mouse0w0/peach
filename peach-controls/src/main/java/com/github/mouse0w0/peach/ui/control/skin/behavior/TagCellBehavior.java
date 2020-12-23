package com.github.mouse0w0.peach.ui.control.skin.behavior;

import com.github.mouse0w0.peach.ui.control.TagCell;
import com.github.mouse0w0.peach.ui.control.TagView;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Collections;

public class TagCellBehavior<T> extends BehaviorBase<TagCell<T>> {
    public TagCellBehavior(TagCell<T> cell) {
        super(cell, Collections.emptyList());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            final TagCell<T> cell = getControl();
            if (cell.isSelected() && e.getClickCount() == 1) {
                cell.startEdit();
            } else if (e.getClickCount() == 2) {
                cell.startEdit();
            }

            final TagView<T> tagView = cell.getTagView();
            if (tagView != null) {
                tagView.getSelectionModel().select(cell.getIndex());
            }
        }
    }
}
