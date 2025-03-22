package com.github.mouse0w0.peach.mcmod.ui.popup;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.ui.GameDataConverter;
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.ui.control.DoubleSpinner;
import com.github.mouse0w0.peach.ui.control.TagCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

public final class AttributeModifierPopup extends PopOver {
    private final ComboBox<String> attribute;
    private final ComboBox<AttributeModifier.Operation> operation;
    private final Spinner<Double> amount;

    private TagCell<AttributeModifier> cell;

    private final InvalidationListener listener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            observable.removeListener(this);
            show(cell);
        }
    };

    public AttributeModifierPopup(Index<String, GameData> attributeIndex) {
        getRoot().minHeightProperty().unbind();
        setArrowLocation(ArrowLocation.TOP_CENTER);
        setAnimated(false);

        GridPane grid = new GridPane();
        grid.setHgap(9);
        grid.setVgap(9);
        grid.setPadding(new Insets(9));
        setContentNode(grid);

        attribute = new ComboBox<>();
        attribute.setPrefWidth(150);
        attribute.setItems(attributeIndex.keyList());
        attribute.setConverter(GameDataConverter.create(attributeIndex));
        grid.addRow(0, new Text(AppL10n.localize("attributeModifier.attribute")), attribute);

        operation = new ComboBox<>();
        operation.setPrefWidth(150);
        operation.getItems().addAll(AttributeModifier.Operation.values());
        operation.setConverter(LocalizableConverter.instance());
        grid.addRow(1, new Text(AppL10n.localize("attributeModifier.operation")), operation);

        amount = new DoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 0);
        amount.setPrefWidth(150);
        grid.addRow(2, new Text(AppL10n.localize("attributeModifier.amount")), amount);

        setOnHiding(event -> cell.commitEdit(new AttributeModifier(attribute.getValue(), amount.getValue(), operation.getValue())));
    }

    public void edit(TagCell<AttributeModifier> cell) {
        this.cell = cell;

        AttributeModifier attributeModifier = cell.getItem();
        attribute.setValue(attributeModifier.getAttribute());
        operation.setValue(attributeModifier.getOperation());
        amount.getValueFactory().setValue(attributeModifier.getAmount());

        if (cell.isNeedsLayout()) {
            cell.needsLayoutProperty().addListener(listener);
        } else {
            show(cell);
        }
    }
}
