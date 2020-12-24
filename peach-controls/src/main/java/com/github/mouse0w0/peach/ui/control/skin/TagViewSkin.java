package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.TagCell;
import com.github.mouse0w0.peach.ui.control.TagView;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TagViewSkin<T> extends SkinBase<TagView<T>> {

    private final FlowPane flow;
    private final List<TagCell<T>> cellList = new ArrayList<>();
    private final StackPane addBtn;

    public TagViewSkin(TagView<T> tagView) {
        super(tagView);

        flow = new FlowPane();
        flow.getStyleClass().add("flow");
        getChildren().add(flow);

        StackPane add = new StackPane();
        add.getStyleClass().add("add");

        addBtn = new StackPane(add);
        addBtn.getStyleClass().add("add-button");
        addBtn.setCursor(Cursor.HAND);
        addBtn.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                TagView<T> skinnable = getSkinnable();
                skinnable.fireEvent(new TagView.TagEvent<>(
                        skinnable, TagView.addEvent(), skinnable.getItems().size()));
                event.consume();
            }
        });
        flow.getChildren().add(addBtn);

        tagView.editingIndexProperty().addListener((observable, oldValue, newValue) -> {
            int oldIndex = oldValue.intValue();
            if (oldIndex != -1) {
                TagCell<T> cell = getCell(oldIndex);
                if (cell != null) cell.cancelEdit();
            }

            int newIndex = newValue.intValue();
            if (newIndex != -1) {
                TagCell<T> cell = getCell(newIndex);
                if (cell != null) cell.startEdit();
            }
        });

        createCell(tagView.getItems().size());
        tagView.getItems().addListener(this::onChildrenChanged);
    }

    public Node getAddButton() {
        return addBtn;
    }

    private TagCell<T> getCell(int index) {
        if (index < 0 || index >= cellList.size()) return null;
        return cellList.get(index);
    }

    private void onChildrenChanged(ListChangeListener.Change<? extends T> c) {
        while (c.next()) {
            if (c.wasPermutated() || c.wasUpdated()) {
                for (int i = c.getFrom(), end = c.getTo(); i < end; i++) {
                    cellList.get(i).updateItem(i);
                }
            } else {
                final int size = c.getList().size();
                if (c.wasAdded()) createCell(size - cellList.size());
                for (int i = c.getFrom(); i < size + c.getRemovedSize(); i++) {
                    cellList.get(i).updateItem(i);
                }
            }
        }
    }

    private void createCell(int count) {
        final TagView<T> skinnable = getSkinnable();
        final Function<TagView<T>, TagCell<T>> cellFactory = skinnable.getCellFactory();
        for (int i = 0; i < count; i++) {
            int index = cellList.size();
            TagCell<T> cell = cellFactory != null ? cellFactory.apply(skinnable) : createDefaultCell();
            cell.updateTagView(skinnable);
            cell.updateIndex(index);
            flow.getChildren().add(index, cell);
            cellList.add(cell);
        }
    }

    private TagCell<T> createCell() {
        final TagView<T> skinnable = getSkinnable();
        final Function<TagView<T>, TagCell<T>> cellFactory = skinnable.getCellFactory();
        final TagCell<T> cell = cellFactory != null ? cellFactory.apply(skinnable) : createDefaultCell();
        final int index = cellList.size();
        cell.updateTagView(skinnable);
        cell.updateIndex(index);
        flow.getChildren().add(index, cell);
        cellList.add(cell);
        return cell;
    }

    private TagCell<T> createDefaultCell() {
        return new TagCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (item instanceof Node) {
                        setText(null);
                        setGraphic((Node) item);
                    } else {
                        setText(item == null ? "null" : item.toString());
                        setGraphic(null);
                    }
                }
            }
        };
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + flow.prefWidth(-1) + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + flow.prefHeight(-1) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        flow.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
    }
}
