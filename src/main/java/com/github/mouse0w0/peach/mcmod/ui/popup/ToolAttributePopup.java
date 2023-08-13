package com.github.mouse0w0.peach.mcmod.ui.popup;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.ToolType;
import com.github.mouse0w0.peach.ui.control.TagCell;
import com.github.mouse0w0.peach.ui.util.Spinners;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;

public final class ToolAttributePopup extends PopOver {
    private final ChoiceBox<String> type;
    private final Spinner<Integer> level;

    private TagCell<ToolAttribute> cell;

    private final InvalidationListener listener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            observable.removeListener(this);
            show(cell);
        }
    };

    public ToolAttributePopup() {
        getRoot().minHeightProperty().unbind();
        setArrowLocation(ArrowLocation.TOP_CENTER);
        setAnimated(false);

        GridPane grid = new GridPane();
        grid.setHgap(9);
        grid.setVgap(9);
        grid.setPadding(new Insets(9));
        setContentNode(grid);

        type = new ChoiceBox<>();
        type.setPrefWidth(150);
        type.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return ToolType.getLocalizedName(object);
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        type.getItems().addAll(ToolType.getToolTypes());
        grid.addRow(0, new Text(AppL10n.localize("toolAttribute.type")), type);

        level = Spinners.create(0, Integer.MAX_VALUE, 0);
        level.setPrefWidth(150);
        grid.addRow(1, new Text(AppL10n.localize("toolAttribute.level")), level);

        setOnHiding(event -> cell.commitEdit(new ToolAttribute(type.getValue(), level.getValue())));
    }

    public void edit(TagCell<ToolAttribute> cell) {
        this.cell = cell;

        ToolAttribute toolAttribute = cell.getItem();
        type.setValue(toolAttribute.getType());
        level.getValueFactory().setValue(toolAttribute.getLevel());

        if (cell.isNeedsLayout()) {
            cell.needsLayoutProperty().addListener(listener);
        } else {
            show(cell);
        }
    }
}
