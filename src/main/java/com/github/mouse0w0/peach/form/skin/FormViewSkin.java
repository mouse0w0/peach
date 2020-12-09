package com.github.mouse0w0.peach.form.skin;

import com.github.mouse0w0.peach.form.DisplayMode;
import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.form.FormView;
import com.github.mouse0w0.peach.form.Group;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FormViewSkin extends SkinBase<FormView> {

    private final ScrollPane scrollPane;
    private final VBox content;

    public FormViewSkin(FormView formView) {
        super(formView);

        consumeMouseEvents(false);

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        getChildren().add(scrollPane);

        content = new VBox();
        content.getStyleClass().setAll("group-box");
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setContent(content);

        formView.formProperty().addListener(observable -> updateGroup());
        updateGroup();
    }

    private void updateGroup() { // low performance
        ObservableList<Node> children = content.getChildren();
        children.clear();
        for (Group group : getSkinnable().getForm().getGroups()) {
            children.add(new GroupSkin(group));
        }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + scrollPane.minWidth(height) + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + scrollPane.minHeight(width) + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + scrollPane.prefWidth(height) + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + scrollPane.prefHeight(width) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(scrollPane, contentX, contentY, contentWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
    }

    private static class GroupSkin extends TitledPane {
        private final Group group;

        private final GridPane grid;

        public GroupSkin(Group group) {
            this.group = group;

            setFocusTraversable(false);
            setMaxWidth(Double.MAX_VALUE);

            textProperty().bind(group.textProperty());
            collapsibleProperty().bind(group.collapsibleProperty());
            expandedProperty().bindBidirectional(group.expandedProperty());
            visibleProperty().bind(group.visibleProperty());
            managedProperty().bind(group.visibleProperty());
            idProperty().bind(group.idProperty());
            Bindings.bindContent(getStyleClass(), group.getStyleClass());

            grid = new GridPane();
            grid.getStyleClass().setAll("grid");
            grid.setAlignment(Pos.CENTER_LEFT);
            for (int i = 0; i < 12; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100D / 12);
                grid.getColumnConstraints().add(colConst);
            }
            setContent(grid);

            updateElement();
        }

        public Group getGroup() {
            return group;
        }

        private void updateElement() { // low performance
            grid.getChildren().clear();
            int row = 0;
            int column = 0;
            for (Element element : group.getElements()) {
                int colSpan = element.getColSpan().getSpan();
                if (column + colSpan > 12 || column % colSpan != 0) {
                    column = 0;
                    row++;
                }
                grid.add(new ElementSkin(element), column, row, colSpan, 1);
                column += colSpan;
            }
        }
    }

    private static class ElementSkin extends Region {
        private final Element element;

        private final Label label;
        private final Node editor;

        private ElementSkin(Element element) {
            this.element = element;

            visibleProperty().bind(element.visibleProperty());
            managedProperty().bind(element.visibleProperty());
            idProperty().bind(element.idProperty());
            Bindings.bindContent(getStyleClass(), element.getStyleClass());

            label = new Label();
            label.setWrapText(true);
            label.textProperty().bind(element.textProperty());

            editor = element.getEditor();

            getChildren().addAll(label, editor);

            element.displayModeProperty().addListener(observable -> updateDisplayMode());
            updateDisplayMode();
        }

        public Element getElement() {
            return element;
        }

        private void updateDisplayMode() {
            boolean labelVisible = element.getDisplayMode() != DisplayMode.EDITOR_ONLY;
            label.setVisible(labelVisible);
            label.setManaged(labelVisible);
        }

        @Override
        protected double computePrefWidth(double height) {
            final DisplayMode displayMode = element.getDisplayMode();
            if (displayMode == DisplayMode.VERTICAL) {
                return snappedLeftInset() + Math.max(label.prefWidth(-1), editor.prefWidth(-1)) + snappedRightInset();
            } else if (displayMode == DisplayMode.EDITOR_ONLY) {
                return snappedLeftInset() + editor.prefWidth(-1) + snappedRightInset();
            } else {
                return snappedLeftInset() + label.prefWidth(-1) + editor.prefWidth(-1) + snappedRightInset();
            }
        }

        @Override
        protected double computePrefHeight(double width) {
            final DisplayMode displayMode = element.getDisplayMode();
            if (displayMode == DisplayMode.VERTICAL) {
                return snappedTopInset() + label.prefHeight(-1) + editor.prefWidth(-1) + snappedBottomInset();
            } else if (displayMode == DisplayMode.EDITOR_ONLY) {
                return snappedTopInset() + editor.prefWidth(-1) + snappedBottomInset();
            } else {
                return snappedTopInset() + Math.max(label.prefHeight(-1), editor.prefHeight(-1)) + snappedBottomInset();
            }
        }

        @Override
        protected void layoutChildren() {
            final double x = snappedLeftInset();
            final double y = snappedTopInset();
            final double width = getWidth();
            final double height = getHeight();
            final double contentWidth = width - x - snappedRightInset();
            final double contentHeight = height - y - snappedBottomInset();

            final DisplayMode displayMode = element.getDisplayMode();
            if (displayMode == DisplayMode.VERTICAL) {
                final double labelHeight = label.prefHeight(-1);
                layoutInArea(label, x, y, contentWidth, labelHeight, 0, HPos.LEFT, VPos.TOP);
                layoutInArea(editor, x, y + labelHeight, contentWidth, contentHeight - labelHeight, 0, HPos.LEFT, VPos.TOP);
            } else if (displayMode == DisplayMode.EDITOR_ONLY) {
                layoutInArea(editor, x, y, contentWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
            } else {
                final double cellWidth = width / element.getColSpan().getSpan();
                final double labelWidth = cellWidth * 2 - x;
                layoutInArea(label, x, y, labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
                layoutInArea(editor, x + labelWidth, y, contentWidth - labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
            }
        }
    }
}
