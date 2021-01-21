package com.github.mouse0w0.peach.mcmod.ui.popup;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.javafx.Spinners;
import com.github.mouse0w0.peach.javafx.control.TagCell;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.ToolType;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;

public class ToolAttributePopup extends PopOver {
    private final ChoiceBox<ToolType> type;
    private final Spinner<Integer> level;

    private TagCell<ToolAttribute> cell;

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
        type.setConverter(new StringConverter<ToolType>() {
            @Override
            public String toString(ToolType object) {
                return object.getLocalizedName();
            }

            @Override
            public ToolType fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        type.getItems().addAll(ToolType.getToolTypes());
        grid.addRow(0, new Text(I18n.translate("toolAttribute.type")), type);

        level = Spinners.create(0, Integer.MAX_VALUE, 0);
        level.setPrefWidth(150);
        grid.addRow(1, new Text(I18n.translate("toolAttribute.level")), level);

        setOnHiding(event -> cell.commitEdit(new ToolAttribute(type.getValue().getName(), level.getValue())));
    }

    public void edit(TagCell<ToolAttribute> cell) {
        this.cell = cell;

        ToolAttribute toolAttribute = cell.getItem();
        type.setValue(ToolType.of(toolAttribute.getType()));
        level.getValueFactory().setValue(toolAttribute.getLevel());

        show(cell);
    }
}
