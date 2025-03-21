package com.github.mouse0w0.peach.mcmod.ui.popup;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.GameDataConverter;
import com.github.mouse0w0.peach.ui.control.IntegerSpinner;
import com.github.mouse0w0.peach.ui.control.TagCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

public final class ToolAttributePopup extends PopOver {
    private final ComboBox<String> type;
    private final Spinner<Integer> level;

    private TagCell<ToolAttribute> cell;

    private final InvalidationListener listener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            observable.removeListener(this);
            show(cell);
        }
    };

    public ToolAttributePopup(Index<String, GameData> toolTypeIndex) {
        getRoot().minHeightProperty().unbind();
        setArrowLocation(ArrowLocation.TOP_CENTER);
        setAnimated(false);

        GridPane grid = new GridPane();
        grid.setHgap(9);
        grid.setVgap(9);
        grid.setPadding(new Insets(9));
        setContentNode(grid);

        type = new ComboBox<>();
        type.setPrefWidth(150);
        type.setItems(toolTypeIndex.keyList().filtered(s -> !"NONE".equals(s)));
        type.setConverter(GameDataConverter.create(toolTypeIndex));
        grid.addRow(0, new Text(AppL10n.localize("toolAttribute.type")), type);

        level = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
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
