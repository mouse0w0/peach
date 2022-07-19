package com.github.mouse0w0.peach.javafx.control.skin.behavior;

import com.github.mouse0w0.peach.javafx.control.TagCell;
import com.github.mouse0w0.peach.javafx.control.TagView;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.inputmap.InputMap;
import javafx.scene.input.MouseEvent;

public class TagCellBehavior<T> extends BehaviorBase<TagCell<T>> {
    private final InputMap<TagCell<T>> inputMap;

    public TagCellBehavior(TagCell<T> cell) {
        super(cell);

        inputMap = createInputMap();

        addDefaultMapping(inputMap,
                new InputMap.MouseMapping(MouseEvent.MOUSE_PRESSED, this::mousePressed));
    }

    @Override
    public InputMap<TagCell<T>> getInputMap() {
        return inputMap;
    }

    protected void mousePressed(MouseEvent e) {
        final TagCell<T> cell = getNode();
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
